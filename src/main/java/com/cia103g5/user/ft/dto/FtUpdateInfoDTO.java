package com.cia103g5.user.ft.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FtUpdateInfoDTO {
    private Integer ftId;
    private String nickname;    // 占卜師用名

    @Min(value = 0, message = "價格必須是大於零的正整數")
    private Integer price;      // 服務價格
    private String intro;       // 占卜師簡介
}
