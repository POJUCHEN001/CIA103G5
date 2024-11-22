package com.cia103g5.user.adminPermission.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AdminPermissionRepository extends JpaRepository<AdminPermissionVo, Integer> {


    @Query("SELECT p.permissionType.permNode " +
            "FROM AdminPermissionVo p " +
            "WHERE p.adminVo.adminId = :adminId")
    Set<String> findPermissionNodesByAdminId(@Param("adminId") Integer adminId);


    @Query("SELECT apt FROM AdminPermissionType apt")
    List<AdminPermissionType> findByAdminPermAll();



    @Modifying
    @Query("DELETE FROM AdminPermissionVo ap WHERE ap.adminVo.adminId = :adminId AND ap.permissionType.permNode = :permNode")
    void deleteByAdminIdAndPermNode(@Param("adminId") Integer adminId, @Param("permNode") String permNode);



}
