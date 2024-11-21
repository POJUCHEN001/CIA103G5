package com.cia103g5.common.exception;

import lombok.Getter;


/**##################################
 #                                  #
 #          自定義拋出錯誤            #
 #      ( 會由 ＡＯＰ 攔截處理 )       #
 #                                  #
 ###################################*/

@Getter
public class ValidationException extends RuntimeException {
    private final Integer code;

    public ValidationException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
