package com.atguigu.gulimall.search.controller;

import com.atguigu.gulimall.common.exception.GulimallException;
import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.F;
import com.atguigu.gulimall.search.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/es")
@RestController
@Slf4j
public class ElasticSearchController {

    @Resource
    private ElasticSearchService esService;
    @PostMapping("/product/save")
    public F<Object> saveProductData(@RequestBody List<SkuEsModel> list){
        boolean hasFailures = false;
        try {
            hasFailures = esService.saveProductData(list);
        } catch (Exception e) {
            log.error("商品上架服务异常: {}", e.getMessage());
            e.printStackTrace();
            return F.error(GulimallException.PRODUCT_UP_EXCEPTION.getCode(), GulimallException.PRODUCT_UP_EXCEPTION.getMessage());
        }
        if (hasFailures) {
            return F.error(GulimallException.PRODUCT_UP_EXCEPTION.getCode(), GulimallException.PRODUCT_UP_EXCEPTION.getMessage());
        } else {
            return F.ok();
        }

    }
}
