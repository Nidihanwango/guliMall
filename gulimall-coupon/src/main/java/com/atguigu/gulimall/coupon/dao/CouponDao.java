package com.atguigu.gulimall.coupon.dao;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:50:02
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
