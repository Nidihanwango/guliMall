package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PurchaseDetailDoneVo {
    @NotNull
    private Long itemId;

    private int status;
    private String reason;
}
