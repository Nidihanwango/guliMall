package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 品牌分类关联
 * 
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:43:44
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    public void updateByCatelogId(@Param("catId") Long catId, @Param("catName") String catName);
}
