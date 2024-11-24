package com.cia103g5.user.complaintCase.model;

import com.cia103g5.aop.exception.ValidationException;
import com.cia103g5.common.utils.PageResponse;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**############################
 #                            #
 #      客服系統 service       #
 #                            #
 ############################ */

@Service
@Transactional
public class ComplaintCaseServiceImpl implements ComplaintCaseService {

    @Autowired
    private ComplaintCaseRepository complaintCaseRepository;

    @Override
    public List<ComplaintCaseDto> findAllComplaintCases() {
        List<ComplaintCaseDto> caseDto = complaintCaseRepository.findAllComplaintCases();
        System.out.println(caseDto.get(34));
        return caseDto.stream()
                .sorted(Comparator.comparing(ComplaintCaseDto::getCaseNo))
                .toList();
    }

    /**
     * 依據頁數查詢資料
     *
     * @param page 頁數
     * @param size 筆數
     * @return
     */
    @Override
    public PageResponse<ComplaintCaseDto> findAllComplaintCases(int page, int size, Map<String, String> condition) {
        Integer caseNo;
        Integer memId;
        Integer adminId;
        Integer status;
        try {
            caseNo = condition.get("caseNo") == null || condition.get("caseNo").isBlank() ? null : Integer.parseInt(condition.get("caseNo"));
            memId = condition.get("memId") == null || condition.get("memId").isBlank() ? null : Integer.parseInt(condition.get("memId"));
            adminId = condition.get("adminId") == null || condition.get("adminId").isBlank() ? null : Integer.parseInt(condition.get("adminId"));
            status = condition.get("status") == null || condition.get("status").isBlank() ? null : Integer.parseInt(condition.get("status"));
        } catch (NumberFormatException e) {
            throw new ValidationException(400, "條件轉換失敗: 請確保條件值為正確的整數");
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "caseNo")); // 按 caseNo 排序
        Page<ComplaintCaseDto> pageRQ = complaintCaseRepository.findAllComplaintCases(caseNo, memId, adminId, status, pageable);

        return new PageResponse<>(
                (int) pageRQ.getTotalElements(),
                pageRQ.getTotalPages(),
                pageRQ.getContent(),
                page,
                size
        );
    }

    @Override
    public List<ComplaintCase> findAll() {
        return List.of();
    }

    @Override
    public ComplaintCase findById(Integer id) {
        ComplaintCase cc = complaintCaseRepository.findById(id).orElse(null);
        if (cc == null) throw new ValidationException(400, "未找到案件");
        return cc;
    }

    @Override
    public ComplaintCase save(ComplaintCase complaintCase) {
        return complaintCaseRepository.save(complaintCase);
    }


    @Override
    public ComplaintCase update(ComplaintCase complaintCase) {
        return complaintCaseRepository.save(complaintCase);
    }


    /**
     * 搜索查詢，但沒有分頁
     * @param caseId
     * @param adminId
     * @param caseType
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<ComplaintCase> searchComplaintCase(Integer caseId, Integer adminId, Integer caseType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<ComplaintCase> spec = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (caseId != null) {
                predicates.add(builder.equal(root.get("caseId"), caseId));
            }
            if (adminId != null) {
                predicates.add(builder.equal(root.get("adminId"), adminId));
            }
            if (caseType != null) {
                predicates.add(builder.equal(root.get("caseType"), caseType));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };

        return complaintCaseRepository.findAll(spec, pageable);
    }

    @Override
    public void saveComplaintCase(Map<String, String> data) {
        String caseNo = data.get("caseNo");
        String caseResult = data.get("caseResult");
        String status = data.get("status");
        if (caseNo == null) throw new ValidationException(400, "未提供案件編號");
        if (caseResult == null) throw new ValidationException(400, "未提供處理結果");
        if (status == null) throw new ValidationException(400, "未提供狀態");
        ComplaintCase complaintCase = complaintCaseRepository.findById(Integer.parseInt(data.get("caseNo"))).orElse(null);
        if (complaintCase == null) throw new ValidationException(400, "未找到案件編號");
        complaintCase.setCaseResult(caseResult);
        complaintCase.setStatus(Integer.valueOf(status));
        complaintCase.setCompletedAt(new Date());
        complaintCaseRepository.save(complaintCase);
    }

}
