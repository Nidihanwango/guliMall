package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 12:43:26
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
