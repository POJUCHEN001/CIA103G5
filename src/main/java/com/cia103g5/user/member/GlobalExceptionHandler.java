package com.cia103g5.user.member;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 處理非法參數例外
	@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseForMember> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponseForMember apiResponseForMember = new ApiResponseForMember("參數錯誤", ex.getMessage());
        return ResponseEntity.badRequest().body(apiResponseForMember);
    }

	// 處理運行時例外
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseForMember> handleRuntimeException(RuntimeException ex) {
        ApiResponseForMember apiResponseForMember = new ApiResponseForMember("系統錯誤", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseForMember);
    }
    
    // 處理會員未找到的例外
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponseForMember> handleMemberNotFoundException(MemberNotFoundException ex, WebRequest request) {
        ApiResponseForMember error = new ApiResponseForMember(
            "會員未找到",
            request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    // 處理所有其他例外
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
//        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
//        ErrorResponse error = new ErrorResponse(
//            "系統錯誤",
//            "發生未預期的錯誤，請聯繫管理員！"
//        );
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }

}