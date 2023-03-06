package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.CategoryBrandRelationDao;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.BrandDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Resource
    private CategoryBrandRelationDao cbrDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        if (StringUtils.isEmpty(key)) {
            IPage<BrandEntity> page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    new QueryWrapper<BrandEntity>()
            );
            return new PageUtils(page);
        } else {
            QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("brand_id", key).or().like("name", key).or().like("descript", key).or().like("first_letter", key);
            IPage<BrandEntity> page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    queryWrapper
            );
            return new PageUtils(page);
        }
    }

    @Override
    public void updateByIdDetail(BrandEntity brand) {
        // 判断品牌名有没有更新
        String name = brand.getName();
        if (!StringUtils.isEmpty(name)){
            CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
            relationEntity.setBrandId(brand.getBrandId());
            relationEntity.setBrandName(name);
            UpdateWrapper<CategoryBrandRelationEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("brand_id", brand.getBrandId()).set("brand_name", name);
            cbrDao.update(relationEntity, wrapper);
        }
        baseMapper.updateById(brand);
    }

}