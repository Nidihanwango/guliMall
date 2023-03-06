package com.atguigu.gulimall.search.service;

import com.atguigu.gulimall.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

public interface ElasticSearchService {
    boolean saveProductData(List<SkuEsModel> list) throws IOException;

}
