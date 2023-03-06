package com.atguigu.gulimall.ware.service.impl;

import com.atguigu.gulimall.common.constant.WarePurchaseDetailEnum;
import com.atguigu.gulimall.common.constant.WarePurchaseEnum;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.ware.dao.WareSkuDao;
import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;
import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.atguigu.gulimall.ware.feign.ProductFeignService;
import com.atguigu.gulimall.ware.service.PurchaseDetailService;
import com.atguigu.gulimall.ware.vo.MergeVo;
import com.atguigu.gulimall.ware.vo.PurchaseDetailDoneVo;
import com.atguigu.gulimall.ware.vo.PurchaseDoneVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.ware.dao.PurchaseDao;
import com.atguigu.gulimall.ware.entity.PurchaseEntity;
import com.atguigu.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Resource
    private PurchaseDetailService purchaseDetailService;
    @Resource
    private WareSkuDao wareSkuDao;

    @Resource
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryUnreceivedList(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1)
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void mergePurchaseNeeds(MergeVo mergeVo) {
        // 合并采购需求 到 采购单
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null || purchaseId == 0) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WarePurchaseEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        final Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = mergeVo.getItems().stream().map(id -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(id);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WarePurchaseDetailEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);
    }

    @Override
    @Transactional
    public void receivePubchase(List<Long> ids) {
        // 1.校验参数
        if (ids == null || ids.size() == 0) {
            return;
        }
        // 2.根据ids获取所有采购单
        List<PurchaseEntity> collect = ids.stream().map(this::getById).filter(item -> {
            // 2.1 过滤掉状态不符合条件的 Status != CREATED || RECEIVED
            return item.getStatus() == WarePurchaseEnum.CREATED.getCode() || item.getStatus() == WarePurchaseEnum.ASSIGNED.getCode();
        }).map(item -> {
            // 2.2 更新采购单的状态和更新时间
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setId(item.getId());
            purchaseEntity.setStatus(WarePurchaseEnum.RECEIVED.getCode());
            purchaseEntity.setUpdateTime(new Date());
            return purchaseEntity;
        }).collect(Collectors.toList());
        this.updateBatchById(collect);
        // 3.根据采购单id获取采购单下所有需求
        collect.forEach(item -> {
            List<PurchaseDetailEntity> list = purchaseDetailService.listByPurchaseId(item.getId());
            List<PurchaseDetailEntity> purchaseDetailEntityList = list.stream().map(purchaseDetailEntity -> {
                PurchaseDetailEntity newPurchaseDetailEntity = new PurchaseDetailEntity();
                newPurchaseDetailEntity.setId(purchaseDetailEntity.getId());
                newPurchaseDetailEntity.setStatus(WarePurchaseDetailEnum.PURCHASING.getCode());
                return newPurchaseDetailEntity;
            }).collect(Collectors.toList());
            // 4.更新所有采购需求的状态
            purchaseDetailService.updateBatchById(purchaseDetailEntityList);
        });
    }

    @Override
    @Transactional
    public void purchaseDone(PurchaseDoneVo purchaseDoneVo) {
        // 设置标志位 如果为false则没有异常,如果为true,则采购项中存在异常
        boolean hasError = false;
        // 1.更新采购需求的状态
        List<PurchaseDetailDoneVo> items = purchaseDoneVo.getItems();
        List<PurchaseDetailEntity> purchaseDetailEntityList = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item.getItemId());
            purchaseDetailEntity.setStatus(item.getStatus());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(purchaseDetailEntityList);
        // 2.更新采购单的状态
        for (PurchaseDetailEntity purchaseDetailEntity : purchaseDetailEntityList) {
            if (purchaseDetailEntity.getStatus() == WarePurchaseDetailEnum.FAILED.getCode()) {
                hasError = true;
                break;
            }
        }
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseDoneVo.getId());
        purchaseEntity.setUpdateTime(new Date());
        if (hasError) {
            purchaseEntity.setStatus(WarePurchaseEnum.ERROR.getCode());
        } else {
            purchaseEntity.setStatus(WarePurchaseEnum.FINISHED.getCode());
        }
        this.updateById(purchaseEntity);
        // 3.更新商品库存
        purchaseDoneVo.getItems().forEach(item -> {
            if (item.getStatus() == WarePurchaseDetailEnum.FINISHED.getCode()) {
                PurchaseDetailEntity purchaseDetailEntity = purchaseDetailService.getById(item.getItemId());
                if (purchaseDetailEntity != null) {
                    Long skuId = purchaseDetailEntity.getSkuId();
                    Integer skuNum = purchaseDetailEntity.getSkuNum();
                    Long wareId = purchaseDetailEntity.getWareId();
                    WareSkuEntity wareSkuEntity = wareSkuDao.selectOne(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
                    if (wareSkuEntity == null) {
                        WareSkuEntity newWareSkuEntity = new WareSkuEntity();
                        newWareSkuEntity.setSkuId(skuId);
                        newWareSkuEntity.setWareId(wareId);
                        newWareSkuEntity.setStock(skuNum);
                        newWareSkuEntity.setStockLocked(0);
                        // 远程调用product模块查询skuName
                        try {
                            R info = productFeignService.info(skuId);
                            if (info.getCode() == 0) {
                                Map<String, Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                                newWareSkuEntity.setSkuName((String) skuInfo.get("skuName"));
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        wareSkuDao.insert(newWareSkuEntity);
                    } else {
                        wareSkuDao.updateStockBySkuIdAndWareId(skuId, wareId, skuNum);
                    }
                }
            }
        });
    }
}