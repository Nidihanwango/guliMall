package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:43:44
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
