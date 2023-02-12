package com.atguigu.gulimall.coupon;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.atguigu.gulimall.coupon.service.CouponService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class GulimallCouponApplicationTest {
    @Autowired
    private CouponService couponService;

    @Test
    public void test1(){
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setNum(50);
        couponService.update(couponEntity, new QueryWrapper<CouponEntity>().eq("id", 1));
    }
}
