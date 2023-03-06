package com.atguigu.gulimall.ware.service;

import com.atguigu.gulimall.common.to.SkuStockTo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:53:30
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageDetail(Map<String, Object> params);

    List<SkuStockTo> getStock(List<Long> skuIds);

}

