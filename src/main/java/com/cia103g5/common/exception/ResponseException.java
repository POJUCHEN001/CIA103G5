package com.cia103g5.common.exception;

import com.cia103g5.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**#############################################
 #                                             #
 #   AOP 處理 API 使用資料庫時發生異常             #
 #   以 Josn 返回錯誤，告知前端錯誤內容（附上狀態碼 ）#
 #                                             #
 #   ( 專門處理 自定義 異常 )                     #
 #                                             #
 ##############################################*/

@ControllerAdvice
public class ResponseException {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        Integer code = ex.getCode();
        ApiResponse<?> response = ApiResponse.error(code, ex.getMessage());
        return ResponseEntity.status(code).body(response);
    }
}
