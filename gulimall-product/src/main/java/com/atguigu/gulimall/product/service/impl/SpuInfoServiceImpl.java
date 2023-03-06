package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.common.constant.ProductConstant;
import com.atguigu.gulimall.common.to.SkuReductionTo;
import com.atguigu.gulimall.common.to.SkuStockTo;
import com.atguigu.gulimall.common.to.SpuBoundTo;
import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.F;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.product.JsontoVo.*;
import com.atguigu.gulimall.product.entity.*;
import com.atguigu.gulimall.product.feign.GulimallCouponFeignService;
import com.atguigu.gulimall.product.feign.GulimallSearchFeignService;
import com.atguigu.gulimall.product.feign.GulimallWareFeignService;
import com.atguigu.gulimall.product.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;
import com.atguigu.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private SpuImagesService spuImagesService;
    @Resource
    private ProductAttrValueService productAttrValueService;
    @Resource
    private GulimallCouponFeignService gulimallCouponFeignService;
    @Resource
    private SkuInfoService skuInfoService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private BrandService brandService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private GulimallWareFeignService wareFeignService;
    @Resource
    private AttrService attrService;
    @Resource
    private GulimallSearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveSpuJsonVo(SpuJsonVo spuJsonVo) {
        // 1.保存spu基本信息 `pms_spu_info`
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuJsonVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveSpuInfo(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();
        // 2.保存spu描述图 `pms_spu_info_desc`
        List<String> decript = spuJsonVo.getDecript();
        spuInfoDescService.saveSpuDecript(spuId, String.join(",", decript));
        // 3.保存spu的图集 `pms_spu_images`
        List<String> images = spuJsonVo.getImages();
        spuImagesService.saveSpuImages(spuId, images);
        // 4.保存spu的规格参数 `pms_product_attr_value`
        List<BaseAttrs> baseAttrs = spuJsonVo.getBaseAttrs();
        productAttrValueService.saveSpuBaseAttrs(spuId, baseAttrs);
        // 5.保存spu的积分信息 `gulimall_sms` -> `sms_spu_bounds`
        Bounds bounds = spuJsonVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuId);
        R save = gulimallCouponFeignService.save(spuBoundTo);
        if (save.getCode() != 0) {
            log.error("远程调用gulimall-coupon保存spu的积分信息失败");
        }
        // 6.保存spu的sku信息
        List<Skus> skus = spuJsonVo.getSkus();
        skus.forEach(sku -> {
            // 6.1 保存sku基本信息 `pms_sku_info`
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(sku, skuInfoEntity);
            skuInfoEntity.setSkuDesc(String.join(",", sku.getDescar()));
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setSpuId(spuId);
            skuInfoEntity.setCatalogId(spuJsonVo.getCatalogId());
            skuInfoEntity.setBrandId(spuJsonVo.getBrandId());
            String defaultImg = "";
            for (Images image : sku.getImages()) {
                if (image.getDefaultImg() == 1) {
                    defaultImg = image.getImgUrl();
                }
            }
            skuInfoEntity.setSkuDefaultImg(defaultImg);
            skuInfoService.save(skuInfoEntity);
            Long skuId = skuInfoEntity.getSkuId();
            // 6.2 保存sku的图片信息 `pms_sku_images`
            List<Images> skuImages = sku.getImages();
            List<SkuImagesEntity> skuImagesEntityList = skuImages.stream().map(img -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setSkuId(skuId);
                skuImagesEntity.setImgUrl(img.getImgUrl());
                skuImagesEntity.setDefaultImg(img.getDefaultImg());
                skuImagesEntity.setImgSort(0);
                return skuImagesEntity;
            }).filter(item -> {
                return item.getImgUrl() != null && item.getImgUrl().length() > 0;
            }).collect(Collectors.toList());
            skuImagesService.saveBatch(skuImagesEntityList);
            // 6.3 保存sku的销售属性信息 `pms_sku_sale_attr_value`
            List<Attr> saleAttrList = sku.getAttr();
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = saleAttrList.stream().map(saleAttr -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(saleAttr, skuSaleAttrValueEntity);
                skuSaleAttrValueEntity.setSkuId(skuId);
                return skuSaleAttrValueEntity;
            }).collect(Collectors.toList());
            skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntityList);
            // 6.4 保存sku的打折信息 `gulimall_sms` -> `sms_sku_ladder`
            // 6.5 保存sku的满减信息 `gulimall_sms` -> `sms_sku_full_reduction`
            // 6.6 保存sku的会员价格表 `gulimall_sms` -> `sms_member_price`
            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(sku, skuReductionTo);
            skuReductionTo.setSkuId(skuId);
            R saveSkuReduction = gulimallCouponFeignService.saveSkuReduction(skuReductionTo);
            if (saveSkuReduction.getCode() != 0) {
                log.error("远程调用gulimall-coupon保存spu的sku折扣信息失败");
            }
        });
    }

    @Override
    public void saveSpuInfo(SpuInfoEntity spuInfoEntity) {
        baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils listByCondication(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        // 校验参数, 拼接查询条件
        String key = (String) params.get("key");
        String catalogId = (String) params.get("catalogId");
        String brandId = (String) params.get("brandId");
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(item -> {
                item.eq("id", key).or().like("spu_name", key);
            });
        }
        if (!StringUtils.isEmpty(catalogId)) {
            queryWrapper.and(item -> {
                item.eq("catelog_id", catalogId);
            });
        }
        if (!StringUtils.isEmpty(brandId)) {
            queryWrapper.and(item -> {
                item.eq("brand_id", brandId);
            });
        }
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.and(item -> {
                item.eq("publish_status", status);
            });
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void upProductBySpuId(Long spuId) {
        // 1.根据spuId查询skuInfo
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        // 2.获取SkuEsModel.Attrs数据
        // 2.1 根据spuId查询productAttrValue
        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueService.listForSpu(spuId);
        // 2.2 获取相关属性的id
        List<Long> attrIds = productAttrValueEntities.stream().map(ProductAttrValueEntity::getAttrId).collect(Collectors.toList());
        // 2.3 查询每个属性的search_type字段, 若为0则不进行封装
        List<Long> esSkuAttrIds = attrService.listEsSkuAttrId(attrIds);
        HashSet<Long> esSkuAttrIdSet = new HashSet<>(esSkuAttrIds);
        // 2.4 过滤productAttrValueEntities中search_type为0的数据, 并将其封装为SkuEsModel.Attr对象
        List<SkuEsModel.Attr> attrs = productAttrValueEntities.stream().filter(item -> {
            return esSkuAttrIdSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attr attr = new SkuEsModel.Attr();
            BeanUtils.copyProperties(item, attr);
            return attr;
        }).collect(Collectors.toList());

        // 3.查询商品库存情况
        // 3.1 收集所有的skuId
        List<Long> skuIds = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        // 3.2 远程调用gulimall-ware服务, 查询商品库存情况
        Map<Long, Boolean> skuStockMap = null;
        try{
            F<List<SkuStockTo>> feignResult = wareFeignService.getStock(skuIds);
            List<SkuStockTo> skuStockToList = feignResult.getData();
            skuStockMap = skuStockToList.stream().collect(Collectors.toMap(SkuStockTo::getSkuId, SkuStockTo::getHasStock));
        } catch (Exception e) {
            log.error("远程调用gulimall-ware服务失败{}", e);
        }

        Map<Long, Boolean> finalSkuStockMap = skuStockMap;
        List<SkuEsModel> skuEsModelList = skuInfoEntities.stream().map(sku -> {
            // 4.拼装成SkuEsModel
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);
            // 需要自行设置的属性字段 skuPrice skuImg hasStock hotScore brandName catelogName brandImg attrs catelogId
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());
            skuEsModel.setHotScore(0L);
            skuEsModel.setCatelogId(sku.getCatalogId());
            // 设置brandName brandImg `pms_brand`
            BrandEntity brandEntity = brandService.getById(sku.getBrandId());
            if (brandEntity != null) {
                skuEsModel.setBrandName(brandEntity.getName());
                skuEsModel.setBrandImg(brandEntity.getLogo());
            }
            // 查询catelogName `pms_category`
            CategoryEntity categoryEntity = categoryService.getById(sku.getCatalogId());
            if (categoryEntity != null) {
                skuEsModel.setCatelogName(categoryEntity.getName());
            }
            // 设置 SkuEsModel.attrs
            skuEsModel.setAttrs(attrs);
            // 设置库存情况
            if (finalSkuStockMap == null) {
                skuEsModel.setHasStock(true);
            } else {
                skuEsModel.setHasStock(finalSkuStockMap.get(sku.getSkuId()));
            }
            return skuEsModel;
        }).collect(Collectors.toList());
        // 5.发送数据到gulimall-search保存
        F<Object> saveProductDataResult = searchFeignService.saveProductData(skuEsModelList);
        if (saveProductDataResult.getCode() == 0) {
            // 调用成功,修改当前spu状态 `pms_spu_info` publish_status
            SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
            spuInfoEntity.setId(spuId);
            spuInfoEntity.setPublishStatus(ProductConstant.SpuStatus.UP.getCode());
            spuInfoEntity.setUpdateTime(new Date());
            baseMapper.updateById(spuInfoEntity);
        } else {
            // 调用失败,重复调用, 重试机制, 接口幂等性

        }

    }


}