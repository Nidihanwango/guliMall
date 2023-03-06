package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.JsontoVo.BaseAttrs;
import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.ProductAttrValueDao;
import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;
import com.atguigu.gulimall.product.service.ProductAttrValueService;

import javax.annotation.Resource;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Resource
    private AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSpuBaseAttrs(Long spuId, List<BaseAttrs> baseAttrs) {
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(item -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuId);
            productAttrValueEntity.setAttrId(item.getAttrId());
            productAttrValueEntity.setAttrValue(item.getAttrValues());
            productAttrValueEntity.setQuickShow(item.getShowDesc());
            AttrEntity attrEntity = attrDao.selectById(item.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

    @Override
    public List<ProductAttrValueEntity> listForSpu(Long spuId) {
        QueryWrapper<ProductAttrValueEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", spuId);
        return this.list(queryWrapper);
    }

    @Override
    public void updateBySpuId(Long spuId, List<ProductAttrValueEntity> list) {
        this.remove(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        list.forEach(item -> {
            item.setSpuId(spuId);
        });
        this.saveBatch(list);
    }

}