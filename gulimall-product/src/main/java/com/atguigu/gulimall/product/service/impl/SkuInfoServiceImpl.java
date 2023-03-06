package com.atguigu.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.SkuInfoDao;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils listByCandication(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        // 1.校验参数
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(item -> {
                item.eq("sku_id", key).or().like("sku_name", key);
            });
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)) {
            BigDecimal bigDecimal = new BigDecimal(catelogId);
            if (bigDecimal.compareTo(new BigDecimal(0)) > 0) {
                queryWrapper.and(item -> {
                    item.eq("catalog_id", catelogId);
                });
            }
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)) {
            BigDecimal bigDecimal = new BigDecimal(brandId);
            if (bigDecimal.compareTo(new BigDecimal(0)) > 0) {
                queryWrapper.and(item -> {
                    item.eq("brand_id", brandId);
                });
            }
        }
        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            queryWrapper.and(item -> {
                item.ge("price", min);
            });
        }
        String max = (String) params.get("max");
        if (!StringUtils.isEmpty(max)) {
            BigDecimal bigDecimal = new BigDecimal(max);
            if (bigDecimal.compareTo(new BigDecimal(0)) > 0) {
                queryWrapper.and(item -> {
                    item.le("price", max);
                });
            }
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}