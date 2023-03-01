package com.atguigu.gulimall.member.controller;

import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.member.feign.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberCallCouponController {

    @Autowired
    private CouponService couponService;

    @RequestMapping("/gcfm")
    public R getCouponsForMember(){
        return couponService.testFeign();
    }
}
