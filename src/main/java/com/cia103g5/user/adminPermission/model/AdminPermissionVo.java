package com.cia103g5.user.adminPermission.model;

import com.cia103g5.user.admin.model.AdminVo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**######################
 #                      #
 #      管理員權限Vo      #
 #                      #
 ######################*/

@Getter
@Setter
@ToString
@Entity
@Table(name = "admin_permission")
@IdClass(AdminPermissionId.class)
public class AdminPermissionVo {

    @Id
    @Column(name = "admin_id")
    private Integer adminId;  // 與 AdminPermissionId 一致

    @Id
    @Column(name = "perm_node")
    private String permNode;  // 與 AdminPermissionId 一致

    @ManyToOne
    @JoinColumn(name = "admin_id", insertable = false, updatable = false)
    private AdminVo adminVo;

    @ManyToOne
    @JoinColumn(name = "perm_node", insertable = false, updatable = false)
    private AdminPermissionType permissionType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "perm_effect")
    private Date permEffect;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "perm_expire")
    private Date permExpire;
}
