package com.cia103g5.user.complaintCase.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**######################
 #                      #
 #    客服事件類型        #
 #                      #
 ######################*/

@Getter
@Setter
@Entity
@Table(name = "case_type")
public class CaseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer caseTypeNo;

    @Column(name = "case_type_name")
    private String caseTypeName;

    @Column(name = "case_type_desc")
    private String caseTypeDesc;

}
