package com.cia103g5.common.permissions;

import com.cia103g5.aop.exception.ValidationException;
import com.cia103g5.common.jwt.JwtUtil;
import com.cia103g5.user.admin.model.AdminService;
import com.cia103g5.user.admin.model.AdminVo;
import com.cia103g5.user.adminPermission.model.AdminPermissionService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**#############################################
 #                                             #
 #   AOP 攔截請求，驗證用戶權限。                  #
 #   以 Josn 返回錯誤，告知前端錯誤內容（附上狀態碼 ）#
 #                                             #
 ##############################################*/

@Aspect
@Component
public class PermissionsAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminPermissionService permissionsService;

    @Before("@annotation(permissionsAnn)")
    public void checkPermissions(PermissionsAnn permissionsAnn) {
        try {
            // 获取请求头中的 Authorization 字段
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new ValidationException(401 ,"Jwt異常");
            }

            // JWT Token
            String token = authorizationHeader.substring(7);
            Claims claims = jwtUtil.getClaims(token);

            String identity = claims.get("roles", String.class);
            if (!"ADMIN".equals(identity)) {
                throw new ValidationException(401, "用戶非管理員");
            }

            // 管理員 ID
            String userId = claims.get("userId", String.class);
            AdminVo adminVo = adminService.findById(Integer.valueOf(userId));
            if (adminVo == null) {
                throw new ValidationException(401, "管理員不存在");
            }

            // 獲取用戶權限表
            Set<String> permissions = permissionsService.getAdminPermissionsList(adminVo);
            if (permissions == null || permissions.isEmpty()) {
                throw new ValidationException(403, "管理無權限");
            }

            if (permissions.contains("*")) {
                return;
            }

            // 獲取 @ 所需權限
            String[] requiredPermissions = permissionsAnn.value();
            for (String requiredPermission : requiredPermissions) {
                if (!permissions.contains(requiredPermission)) {
                    throw new ValidationException(403, "操作權限不足，需要 " + requiredPermission);
                }
            }




        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(403, "權限驗證異常");
        }
    }

}
