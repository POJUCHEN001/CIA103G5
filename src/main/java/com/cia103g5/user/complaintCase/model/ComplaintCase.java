package com.cia103g5.user.complaintCase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


/**######################
 #                      #
 #   客服案件 model      #
 #                      #
 ######################*/

@Getter
@Setter
@ToString
@Entity
@Table(name = "complaint_case")
public class ComplaintCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_no")
    private Integer caseNo;

    @Column(name = "mem_id")
    private Integer memId;

    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "case_type_no")
    private Integer caseTypeNo;

    @Column(name = "status")
    private Integer status;

    @Column(name = "case_title")
    private String caseTitle;

    @Column(name = "case_content")
    private String caseContent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "filed_time")
    private Date filedAt;

    @Column(name = "case_result")
    private String caseResult;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "completed_time")
    private Date completedAt;


    @Transient
    @JsonProperty
    private String statusSt;


    @PostLoad
    private void initStatusString() {
        switch (this.status) {
            case 0:
                this.statusSt = "待處理";
                break;
            case 1:
                this.statusSt = "處理中";
                break;
            case 2:
                this.statusSt = "已完成";
                break;
            default:
                this.statusSt = "異常";
                break;
        }
    }
}
