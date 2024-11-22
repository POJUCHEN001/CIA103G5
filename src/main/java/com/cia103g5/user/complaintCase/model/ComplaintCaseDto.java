package com.cia103g5.user.complaintCase.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ComplaintCaseDto {
    private Integer caseNo;

    private Integer memId;
    private String memName;

    private Integer adminId;
    private String adminName;

    private Integer caseTypeNo;
    private String caseTypeName;

    private Integer status;

    private String caseTitle;
    private String caseContent;

    private String caseResult;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date filedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date completedAt;

    // 全部屬性參數的建構子
    public ComplaintCaseDto(Integer caseNo, Integer memId, String memName, Integer adminId, String adminName,
                            Integer caseTypeNo, String caseTypeName, Integer status, String caseTitle,
                            String caseContent, String caseResult, Date filedAt, Date completedAt) {
        this.caseNo = caseNo;
        this.memId = memId;
        this.memName = memName;
        this.adminId = adminId;
        this.adminName = adminName;
        this.caseTypeNo = caseTypeNo;
        this.caseTypeName = caseTypeName;
        this.status = status;
        this.caseTitle = caseTitle;
        this.caseContent = caseContent;
        this.caseResult = caseResult;
        this.filedAt = filedAt;
        this.completedAt = completedAt;
        initStatusString();
    }


    private String statusSt;
    private void initStatusString() {
        switch (this.status) {
            case 0:
                this.statusSt = "待處理";
                break;
            case 1:
                this.statusSt = "處理中";
                break;
            case 2:
                this.statusSt = "已結案";
                break;
            default:
                this.statusSt = "異常";
                break;
        }
    }



}
