package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;
import com.atguigu.gulimall.product.vo.AttrResponVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:43:44
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(AttrVo attrVo);

    PageUtils typeListByCatelogId(String attrType, Map<String, Object> params, Long catelogId);

    AttrResponVo getAttrById(Long attrId);


    void updateDetail(AttrVo attrVo);

    List<Long> listEsSkuAttrId(List<Long> attrIds);
}

