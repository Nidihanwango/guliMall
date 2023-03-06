package com.atguigu.gulimall.common.constant;

public enum ProductAttrType {
    BASE(1, "基本属性"), SALE(0, "销售属性");

    private final int code;
    private final String msg;

    ProductAttrType(int code, String msg){
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
