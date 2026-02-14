package com.payline.global.common.dto;

public record ApiResponse<T>(
        boolean success,
        T data,
        String message,
        String errorCode
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static ApiResponse<?> error(String message, String errorCode) {
        return new ApiResponse<>(false, null, message, errorCode);
    }
}
