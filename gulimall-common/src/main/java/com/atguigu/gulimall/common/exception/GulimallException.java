package com.atguigu.gulimall.common.exception;

public enum GulimallException {
    UNKNOW_EXCEPTION(10000, "未知异常"),
    VALID_EXCEPTION(10001, "校验未通过");

    private final int code;
    private final String message;
    GulimallException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
