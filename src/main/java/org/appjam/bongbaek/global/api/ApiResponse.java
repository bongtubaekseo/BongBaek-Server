package org.appjam.bongbaek.global.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.appjam.bongbaek.global.common.ErrorCode;
import org.appjam.bongbaek.global.common.SuccessCode;

public record ApiResponse<T>(
        boolean success,
        int status,
        String message,
        @JsonInclude(value = JsonInclude.Include.NON_NULL)
        T data
) {
    // response body 없는 버전
    public static <T> ApiResponse<T> success(SuccessCode successCode) {
        return new ApiResponse<>(successCode.getSuccess(), successCode.getStatus().value(), successCode.getMessage(), null);
    }

    // response body 있는 버전
    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return new ApiResponse<>(successCode.getSuccess(), successCode.getStatus().value(), successCode.getMessage(), data);
    }

    public static <T> ApiResponse<T> failure(ErrorCode failureCode) {
        return new ApiResponse<>(failureCode.getSuccess(), failureCode.getStatus().value(), failureCode.getMessage(), null);
    }
}
