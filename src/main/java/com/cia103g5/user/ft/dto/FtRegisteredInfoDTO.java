package com.cia103g5.user.ft.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class FtRegisteredInfoDTO {
    private Integer memberId;
    private String companyName;
    private String businessNo;
    private String nickname;
    private MultipartFile photo;
    private MultipartFile businessPhoto;
}
