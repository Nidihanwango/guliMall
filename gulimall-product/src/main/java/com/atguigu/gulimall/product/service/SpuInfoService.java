package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.JsontoVo.SpuJsonVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:43:44
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuJsonVo(SpuJsonVo spuJsonVo);

    void saveSpuInfo(SpuInfoEntity spuInfoEntity);

    PageUtils listByCondication(Map<String, Object> params);

    void upProductBySpuId(Long spuId);
}

