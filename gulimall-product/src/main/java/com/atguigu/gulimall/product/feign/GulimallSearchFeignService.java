package com.atguigu.gulimall.product.feign;

import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.F;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-search")
public interface GulimallSearchFeignService {
    @PostMapping("/es/product/save")
    public F<Object> saveProductData(@RequestBody List<SkuEsModel> list);
}
