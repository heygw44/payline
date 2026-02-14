package com.payline.global.common.dto;

public record ApiResponse<T>(
        boolean success,
        T data,
        String message,
        String errorCode
) {

    public ApiResponse {
        if (success) {
            if (message != null || errorCode != null) {
                throw new IllegalArgumentException("Success response must not contain message or errorCode");
            }
        } else {
            if (data != null) {
                throw new IllegalArgumentException("Error response must not contain data");
            }
            if (message == null || message.isBlank()) {
                throw new IllegalArgumentException("Error response must contain message");
            }
            if (errorCode == null || errorCode.isBlank()) {
                throw new IllegalArgumentException("Error response must contain errorCode");
            }
        }
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(false, null, message, errorCode);
    }
}
