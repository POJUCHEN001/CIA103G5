package com.cia103g5.user.adminPermission.model;

import com.cia103g5.aop.exception.ValidationException;
import com.cia103g5.user.admin.model.AdminVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * #############################
 * #                           #
 * #      管理員權限 service     #
 * #                           #
 * #############################
 */

@Service
public class AdminPermissionService {

    private final String redisKey = "admin_permissions";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AdminPermissionRepository adminPermissionRepository;


    //獲取管理員 權限表
    public Set<String> getAdminPermissionsList(AdminVo adminVo) {
        String hashKey = String.valueOf(adminVo.getAdminId());
        String permissionsJson;

        Set<String> permissions;
        try {
            // 從 Redis 中獲取權限數據
            permissionsJson = (String) redisTemplate.opsForHash().get(redisKey, hashKey);

            if (permissionsJson == null) {
                permissions = adminPermissionRepository.findPermissionNodesByAdminId(adminVo.getAdminId());
                if (permissions == null) permissions = new HashSet<>();

                // 將結果序列化為 JSON 並存儲到 Redis
                permissionsJson = new ObjectMapper().writeValueAsString(permissions);
                redisTemplate.opsForHash().put(redisKey, hashKey, permissionsJson);
                redisTemplate.expire(redisKey, 1, TimeUnit.HOURS); // 設置過期時間
            } else {
                // 如果 Redis 中存在數據，反序列化
                permissions = new ObjectMapper().readValue(permissionsJson, new TypeReference<Set<String>>() {
                });
            }
        } catch (JsonProcessingException e) {
            throw new ValidationException(500, "JSON 轉換異常: " + e.getMessage());
        } catch (Exception e) {
            throw new ValidationException(500, "Redis 操作異常: " + e.getMessage());
        }
        return permissions;
    }


    private List<AdminPermissionType> getPermissionsType() {
        return adminPermissionRepository.findByAdminPermAll();
    }

    // 獲取所有權限，並根據管理員擁有權限設定布林值
    public Map<String, List<AdminPermissionType>> getPermissionsAll(AdminVo adminVo) {

        Set<String> adminPermissionsList = getAdminPermissionsList(adminVo);

        List<AdminPermissionType> adminPermissionTypes = getPermissionsType();

        for (AdminPermissionType adminPermissionType : adminPermissionTypes) {
            adminPermissionType.setHasPermission(adminPermissionsList.contains(adminPermissionType.getPermNode()));
        }
        Map<String, List<AdminPermissionType>> permissions = new HashMap<>();

        if (!adminPermissionTypes.isEmpty()) {
            adminPermissionTypes.forEach(adminPermissionType -> {
                String type = adminPermissionType.getPermType();
                permissions.computeIfAbsent(type, k -> new ArrayList<>()).add(adminPermissionType);
            });
        }

        return permissions;

    }


    /**
     * 管理員權限調整
     * @param adminVo 目標管理員
     * @param permissions 送回來的表單
     */
    public void savePermissions(AdminVo adminVo, Map<String, List<AdminPermissionType>> permissions) {
        // 獲取管理員權限表
        Set<String> adminPermissionsSet = getAdminPermissionsList(adminVo);
        permissions.forEach((key, value) -> {
            value.forEach(adminPermissionType -> {
                // 如果有權限但標記沒有刪除它 如果沒有但標記有新增他
                if (adminPermissionsSet.contains(adminPermissionType.getPermNode()) && !adminPermissionType.getHasPermission()) {
                    adminPermissionRepository.deleteByAdminIdAndPermNode(adminVo.getAdminId(), adminPermissionType.getPermNode());
                }
                if (!adminPermissionsSet.contains(adminPermissionType.getPermNode()) && adminPermissionType.getHasPermission()) {
                    AdminPermissionVo adminPermissionVo = new AdminPermissionVo();
                    adminPermissionVo.setAdminVo(adminVo);
                    adminPermissionVo.setPermissionType(adminPermissionType);
                    adminPermissionVo.setPermEffect(new Date());
                    adminPermissionVo.setPermExpire(Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    adminPermissionRepository.save(adminPermissionVo);
                }
            });
        });

        redisTemplate.opsForHash().delete(redisKey, String.valueOf(adminVo.getAdminId()));
    }

}
