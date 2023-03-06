package com.atguigu.gulimall.product.feign;

import com.atguigu.gulimall.common.to.SkuReductionTo;
import com.atguigu.gulimall.common.to.SpuBoundTo;
import com.atguigu.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-coupon")
public interface GulimallCouponFeignService {
    @PostMapping("/coupon/spubounds/save")
    public R save(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skureduction/saveSkuReduction")
    public R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
