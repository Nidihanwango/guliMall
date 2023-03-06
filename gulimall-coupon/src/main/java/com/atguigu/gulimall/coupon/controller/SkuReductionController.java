package com.atguigu.gulimall.coupon.controller;

import com.atguigu.gulimall.common.to.SkuReductionTo;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.coupon.service.SkuReductionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/coupon/skureduction/")
public class SkuReductionController {

    @Resource
    private SkuReductionService skuReductionService;
    @PostMapping("saveSkuReduction")
    public R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo){
        skuReductionService.saveSkuReduction(skuReductionTo);
        return R.ok();
    }
}
