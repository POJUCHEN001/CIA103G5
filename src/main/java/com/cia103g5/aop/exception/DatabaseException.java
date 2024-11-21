//package com.cia103g5.aop.exception;
//
//import com.cia103g5.common.ApiResponse;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.dao.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.sql.SQLException;
//
//
///**#############################################
// #                                             #
// #   AOP 處理 API 使用資料庫時發生異常             #
// #   以 Josn 返回錯誤，告知前端錯誤內容（附上狀態碼 ）#
// #                                             #
// #   ( 專門處理 sql 異常 )                       #
// #                                             #
// ##############################################*/
//
//
//@ControllerAdvice
//public class DatabaseException {
//
//    // 違反約束
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ApiResponse<?>> dataIntegrityViolation(DataIntegrityViolationException ex){
//        ApiResponse<?> response = ApiResponse.error(409, "違反約束");
//        return ResponseEntity.status(response.getCode()).body(response);
//    }
//
//    @ExceptionHandler(DuplicateKeyException.class)
//    public ResponseEntity<ApiResponse<?>> duplicateKey(DuplicateKeyException ex){
//        ApiResponse<?> response = ApiResponse.error(409, "key已存在");
//        return ResponseEntity.status(response.getCode()).body(response);
//    }
//
//    @ExceptionHandler(DataRetrievalFailureException.class)
//    public ResponseEntity<ApiResponse<?>> dataRetrievalFailure(DataRetrievalFailureException ex){
//        ApiResponse<?> response = ApiResponse.error(404, "獲取異常");
//        return ResponseEntity.status(response.getCode()).body(response);
//    }
//
//    @ExceptionHandler(EmptyResultDataAccessException.class)
//    public ResponseEntity<ApiResponse<?>> emptyResultDataAccess(EmptyResultDataAccessException ex){
//        ApiResponse<?> response = ApiResponse.error(404, "預期失敗");
//        return ResponseEntity.status(response.getCode()).body(response);
//    }
//
//    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
//    public ResponseEntity<ApiResponse<?>> incorrectResultSizeDataAccess(IncorrectResultSizeDataAccessException ex){
//        ApiResponse<?> response = ApiResponse.error(500, "size不符");
//        return ResponseEntity.status(response.getCode()).body(response);
//    }
//
//    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
//    public ResponseEntity<ApiResponse<?>> invalidDataAccessApiUsage(InvalidDataAccessApiUsageException ex){
//        ApiResponse<?> response = ApiResponse.error(400, "API使用異常");
//        return ResponseEntity.status(response.getCode()).body(response);
//    }
//
//    @ExceptionHandler(PermissionDeniedDataAccessException.class)
//    public ResponseEntity<ApiResponse<?>> permissionDeniedDataAccess(PermissionDeniedDataAccessException ex){
//        ApiResponse<?> response = ApiResponse.error(403, "權限不足");
//        return ResponseEntity.status(response.getCode()).body(response);
//    }
//
//    @ExceptionHandler(QueryTimeoutException.class)
//    public ResponseEntity<ApiResponse<?>> queryTimeoutException(QueryTimeoutException ex){
//        ApiResponse<?> response = ApiResponse.error(408, "查詢超時");
//        return ResponseEntity.status(response.getCode()).body(response);
//    }
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<ApiResponse<?>> EntityNotFound(EntityNotFoundException ex){
//        ApiResponse<?> response = ApiResponse.error(404, "未找到實體");
//        return ResponseEntity.status(response.getCode()).body(response);
//    }
//
//    @ExceptionHandler(SQLException.class)
//    public ResponseEntity<ApiResponse<?>> queryTimeout(SQLException ex) {
//        ApiResponse<?> response = ApiResponse.error(503, "其他database異常");
//        return ResponseEntity.status(response.getCode()).body(response);
//    }
//
//}
//
