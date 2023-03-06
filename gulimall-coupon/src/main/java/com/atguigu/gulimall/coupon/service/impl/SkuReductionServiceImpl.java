package com.atguigu.gulimall.coupon.service.impl;

import com.atguigu.gulimall.common.to.MemberPrice;
import com.atguigu.gulimall.common.to.SkuReductionTo;
import com.atguigu.gulimall.coupon.entity.MemberPriceEntity;
import com.atguigu.gulimall.coupon.entity.SkuFullReductionEntity;
import com.atguigu.gulimall.coupon.entity.SkuLadderEntity;
import com.atguigu.gulimall.coupon.service.MemberPriceService;
import com.atguigu.gulimall.coupon.service.SkuFullReductionService;
import com.atguigu.gulimall.coupon.service.SkuLadderService;
import com.atguigu.gulimall.coupon.service.SkuReductionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkuReductionServiceImpl implements SkuReductionService {
    @Resource
    private SkuFullReductionService skuFullReductionService;
    @Resource
    private SkuLadderService skuLadderService;
    @Resource
    private MemberPriceService memberPriceService;
    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        // 6.4 保存sku的打折信息 `gulimall_sms` -> `sms_sku_ladder`
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionTo, skuLadderEntity);
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuLadderEntity.getFullCount() != null && skuLadderEntity.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }
        // 6.5 保存sku的满减信息 `gulimall_sms` -> `sms_sku_full_reduction`
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        skuFullReductionEntity.setAddOther(skuReductionTo.getPriceStatus());
        if (skuFullReductionEntity.getFullPrice() != null && skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal(0)) > 0) {
            skuFullReductionService.save(skuFullReductionEntity);
        }
        // 6.6 保存sku的会员价格表 `gulimall_sms` -> `sms_member_price`
        List<MemberPrice> memberPriceList = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntityList = memberPriceList.stream().map(item -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setMemberPrice(item.getPrice());
            return memberPriceEntity;
        }).filter(item -> {
            return item.getMemberPrice().compareTo(new BigDecimal(0)) > 0;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntityList);
    }
}
