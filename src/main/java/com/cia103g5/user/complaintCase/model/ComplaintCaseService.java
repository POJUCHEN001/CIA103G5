package com.cia103g5.user.complaintCase.model;

import com.cia103g5.common.utils.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**###########################
 #                           #
 #     客服系統 interface     #
 #                           #
 ############################*/

public interface ComplaintCaseService {

    List<ComplaintCaseDto> findAllComplaintCases();
    PageResponse<ComplaintCaseDto> findAllComplaintCases(int page, int size, Map<String, String> condition);

    List<ComplaintCase> findAll();
    ComplaintCase findById(Integer id);
    ComplaintCase save(ComplaintCase complaintCase);
    ComplaintCase update(ComplaintCase complaintCase);
    Page<ComplaintCase> searchComplaintCase(Integer caseId, Integer adminId, Integer caseType, int page, int size);

    void saveComplaintCase(Map<String, String> data);
}
