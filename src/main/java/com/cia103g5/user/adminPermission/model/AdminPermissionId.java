package com.cia103g5.user.adminPermission.model;

import java.io.Serializable;
import java.util.Objects;

public class AdminPermissionId implements Serializable {
    private Integer adminId;  // 與 AdminPermission 對應
    private String permNode;  // 與 AdminPermission 對應

    public AdminPermissionId() {}

    public AdminPermissionId(Integer adminId, String permNode) {
        this.adminId = adminId;
        this.permNode = permNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminPermissionId that = (AdminPermissionId) o;
        return Objects.equals(adminId, that.adminId) && Objects.equals(permNode, that.permNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminId, permNode);
    }
}
