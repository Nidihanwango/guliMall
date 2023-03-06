package com.atguigu.gulimall.common.constant;

public enum WarePurchaseEnum {
    CREATED(0, "新建"), ASSIGNED(1, "已分配"),RECEIVED(2, "已领取"),FINISHED(3, "已完成"),ERROR(4, "有异常");

    private final int code;
    private final String msg;

    WarePurchaseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
