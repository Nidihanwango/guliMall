package com.atguigu.gulimall.common.constant;

public enum WarePurchaseDetailEnum {
    CREATED(0, "新建"), ASSIGNED(1, "已分配"), PURCHASING(2, "正在采购"), FINISHED(3, "已完成"),FAILED(4, "采购失败");


    private final int code;
    private final String msg;

    WarePurchaseDetailEnum(int code, String msg) {
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
