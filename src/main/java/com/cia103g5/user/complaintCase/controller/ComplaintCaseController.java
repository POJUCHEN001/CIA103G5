package com.cia103g5.user.complaintCase.controller;

import com.cia103g5.common.ApiResponse;
import com.cia103g5.common.permissions.PermissionsAnn;
import com.cia103g5.common.utils.PageResponse;
import com.cia103g5.user.complaintCase.model.ComplaintCase;
import com.cia103g5.user.complaintCase.model.ComplaintCaseDto;
import com.cia103g5.user.complaintCase.model.ComplaintCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//開放所有網域使用
@CrossOrigin("*")
//回傳Json
@ResponseBody
@RestController
@RequestMapping("/api/complaintCase")
@PermissionsAnn("admin.complaint.view")
public class ComplaintCaseController {

    @Autowired
    private ComplaintCaseService complaintCaseService;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<ComplaintCase> complaintCases = complaintCaseService.findAll();
        ApiResponse<List<ComplaintCase>> response =  ApiResponse.success(complaintCases);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findAllDto")
    public ResponseEntity<?> findAllDto() {
        List<ComplaintCaseDto> caseDto = complaintCaseService.findAllComplaintCases();
        ApiResponse<List<ComplaintCaseDto>> response = ApiResponse.success(caseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 一分頁給資料
     *
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/findAllDto/{page}/{size}")
    public ResponseEntity<?> findAllDtoByPage(
            @PathVariable int page, @PathVariable int size,
            @RequestBody Map<String, String> params) {
        PageResponse<?> caseDto = complaintCaseService.findAllComplaintCases(page, size, params);
        ApiResponse<PageResponse<?>> response = ApiResponse.success(caseDto);
        System.out.println(response);
        return ResponseEntity.ok(response);
    }


    /**
     * 更新資料
     *
     * @param complaintCase
     * @param caseNo
     * @return
     */
    @PermissionsAnn("admin.complaint.reply")
    @PostMapping("/update/{caseNo}")
    public ResponseEntity<?> update(ComplaintCase complaintCase, @PathVariable Integer caseNo) {
        complaintCase = complaintCaseService.update(complaintCase);
        ApiResponse<ComplaintCase> response = ApiResponse.success(complaintCase);
        return ResponseEntity.ok(response);
    }


    @PermissionsAnn("admin.complaint.reply")
    @PostMapping("/saveComplaint")
    public ResponseEntity<?> save(@RequestBody Map<String, String> data) {
        complaintCaseService.saveComplaintCase(data);
        ApiResponse<?> response = ApiResponse.success(null);
        return ResponseEntity.ok(response);
    }


}
