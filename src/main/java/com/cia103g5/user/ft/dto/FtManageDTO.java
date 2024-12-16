package com.cia103g5.user.ft.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FtManageDTO {
    private Integer ftId;
    private Integer memberId;
    private String nickname;
    private String companyName;
    private String businessNo;
    private MultipartFile photo;
    private MultipartFile businessPhoto;
    private Date registeredTime;
    private Date approvedTime;
    private Byte canPost;
    private Byte canRev;
    private Byte canSell;
    private Date actionStartedDay;
    private Date actionEndedDay;
    private Byte status;
}
