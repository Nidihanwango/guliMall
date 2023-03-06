package com.atguigu.gulimall.ware.dao;

import com.atguigu.gulimall.common.to.SkuStockTo;
import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:53:30
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void updateStockBySkuIdAndWareId(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    Long getStock(Long skuId);
}
