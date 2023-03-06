package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id;

    @NotEmpty
    private List<PurchaseDetailDoneVo> items;
}
