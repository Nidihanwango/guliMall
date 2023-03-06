package com.atguigu.gulimall.common.constant;

public class ProductConstant {
    public enum SpuStatus{
        CREATED(0, "新建"),UP(1, "上架"),DOWN(2, "下架");
        private final int code;
        private final String msg;

        SpuStatus(int code, String msg) {
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
}
