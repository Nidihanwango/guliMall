package com.atguigu.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.search.config.GulimallElasticSearchConfig;
import com.atguigu.gulimall.search.constant.ElasticSearchConstant;
import com.atguigu.gulimall.search.service.ElasticSearchService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
    @Resource
    private RestHighLevelClient client;
    @Override
    public boolean saveProductData(List<SkuEsModel> list) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();

        list.forEach(item -> {
            IndexRequest indexRequest = new IndexRequest(ElasticSearchConstant.PRODUCT_INDEX);
            indexRequest.id(item.getSkuId().toString());
            String s = JSON.toJSONString(item);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        });

        BulkResponse bulk = client.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

        boolean b = bulk.hasFailures();

        List<String> upFailSkuIds = Arrays.stream(bulk.getItems()).map(item -> {
            if (item.isFailed()) {
                return item.getId();
            }
            return null;
        }).collect(Collectors.toList());

        return b;
    }
}
