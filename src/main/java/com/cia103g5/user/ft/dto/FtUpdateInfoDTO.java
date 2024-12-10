package com.cia103g5.user.ft.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FtUpdateInfoDTO {
    private Integer ftId;
    private String nickName;    // 占卜師用名
    private Integer price;      // 服務價格
    private String intro;       // 占卜師簡介
}
