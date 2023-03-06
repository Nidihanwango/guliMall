package com.atguigu.gulimall.product.feign;

import com.atguigu.gulimall.common.to.SkuStockTo;
import com.atguigu.gulimall.common.utils.F;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-ware")
public interface GulimallWareFeignService {

    @PostMapping("/ware/waresku/getStock")
    public F<List<SkuStockTo>> getStock(@RequestBody List<Long> skuIds);
}
