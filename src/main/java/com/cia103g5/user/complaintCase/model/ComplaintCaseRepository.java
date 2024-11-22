package com.cia103g5.user.complaintCase.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintCaseRepository extends JpaRepository<ComplaintCase, Integer>, JpaSpecificationExecutor<ComplaintCase> {

    @Query("SELECT COUNT(c) FROM  ComplaintCase c")
    Integer totoCount();


    @Query("SELECT new org.cia103.dto.ComplaintCaseDto(" +
            "c.caseNo, m.memId, m.nickname, a.adminId, a.name, ct.caseTypeNo, ct.caseTypeName, " +
            "c.status,c.caseTitle, c.caseContent, c.caseResult, c.filedAt, c.completedAt) " +
            "FROM ComplaintCase c " +
            "JOIN MemberVo m ON c.memId = m.memId " +
            "LEFT JOIN AdminVo a ON c.adminId = a.adminId " +
            "JOIN CaseType ct ON c.caseTypeNo = ct.caseTypeNo")
    List<ComplaintCaseDto> findAllComplaintCases();

    @Query("SELECT new org.cia103.dto.ComplaintCaseDto(" +
            "c.caseNo, m.memId, m.nickname, a.adminId, a.name, ct.caseTypeNo, ct.caseTypeName, " +
            "c.status, c.caseTitle, c.caseContent, c.caseResult, c.filedAt, c.completedAt) " +
            "FROM ComplaintCase c " +
            "JOIN MemberVo m ON c.memId = m.memId " +
            "LEFT JOIN AdminVo a ON c.adminId = a.adminId " +
            "JOIN CaseType ct ON c.caseTypeNo = ct.caseTypeNo " +
            "WHERE (:caseNo IS NULL OR c.caseNo = :caseNo) " +
            "AND (:memId IS NULL OR m.memId = :memId) " +
            "AND (:adminId IS NULL OR a.adminId = :adminId) " +
            "AND (:status IS NULL OR c.status = :status)")
    Page<ComplaintCaseDto> findAllComplaintCases(@Param("caseNo") Integer caseNo,
                                                 @Param("memId") Integer memId,
                                                 @Param("adminId") Integer adminId,
                                                 @Param("status") Integer status,
                                                 Pageable pageable);

}
