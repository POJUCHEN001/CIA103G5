package com.cia103g5.common;

import lombok.Getter;
import lombok.Setter;

/**##################################
 #                                  #
 #          將訊息進行封裝            #
 #                                  #
 ###################################*/


@Getter
@Setter
public class ApiResponse<T> {
    private boolean status;
    private int code;
    private String message;
    private T data;

    public ApiResponse(boolean status, Integer code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 響應成功（不傳參數）
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, 200, "操作成功", null);
    }

    // 響應成功（給資料）
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, 200, "操作成功", data);
    }

    // 響應成功
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, 200, message, data);
    }

    /**
     * 響應失敗
     * @param code 錯誤代號
     * @param message 錯誤訊息
     * @return  回傳封裝
     * @param <T>
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

}
