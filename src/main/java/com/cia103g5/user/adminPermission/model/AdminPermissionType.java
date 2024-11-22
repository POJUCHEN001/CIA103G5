package com.cia103g5.user.adminPermission.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "admin_permission_type")
public class AdminPermissionType {

    @Id
    @Column(name = "perm_node")
    String permNode;

    @Column(name = "perm_desc")
    String permDesc;

    @Column(name = "perm_type")
    String permType;

    @Transient
    @JsonProperty
    Boolean hasPermission;

    @Override
    public String toString() {
        return "permNode: " + permNode +
                "\t permDesc: " + permDesc +
                "\t hasPermission: " + hasPermission +
                "\t permType: " + permType +
                " \n";
    }
}
