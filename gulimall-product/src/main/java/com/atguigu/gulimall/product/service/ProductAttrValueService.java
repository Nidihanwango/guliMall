package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.JsontoVo.BaseAttrs;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:43:44
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuBaseAttrs(Long spuId, List<BaseAttrs> baseAttrs);

    List<ProductAttrValueEntity> listForSpu(Long spuId);

    void updateBySpuId(Long spuId, List<ProductAttrValueEntity> list);
}

