package com.cia103g5.user.adminPermission.controller;

import com.cia103g5.common.ApiResponse;
import com.cia103g5.user.admin.model.AdminService;
import com.cia103g5.user.admin.model.AdminVo;
import com.cia103g5.user.adminPermission.model.AdminPermissionService;
import com.cia103g5.user.adminPermission.model.AdminPermissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//回傳Json
@ResponseBody
@RestController
@RequestMapping("/api/admin")
public class AdminPermissionController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminPermissionService adminPermissionService;


    @GetMapping("/getPermissions/{id}")
    public ResponseEntity<ApiResponse<?>> getPermissions(@PathVariable Integer id){
        AdminVo adminVo = adminService.findById(id);

        Map<String, List<AdminPermissionType>> adminPermList = adminPermissionService.getPermissionsAll(adminVo);
        ApiResponse<?> response = ApiResponse.success(adminPermList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/savePermissions/{id}")
    public ResponseEntity<ApiResponse<?>> savePermissions (@RequestBody Map<String, List<AdminPermissionType>> adminPermList, @PathVariable Integer id) {
        AdminVo adminVo = adminService.findById(id);
        adminPermissionService.savePermissions(adminVo, adminPermList);

        ApiResponse<?> response = ApiResponse.success(null);
        return ResponseEntity.ok(response);
    }
}
