package com.k8smaster.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Result<T>(int code, String message, T data) {

    private static final int SUCCESS_CODE = 0;
    private static final int FAILURE_CODE = -1;

    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, "success", data);
    }

    public static Result<Void> success() {
        return new Result<>(SUCCESS_CODE, "success", null);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(FAILURE_CODE, message, null);
    }

    public static <T> Result<T> failure(int code, String message) {
        return new Result<>(code, message, null);
    }
}
