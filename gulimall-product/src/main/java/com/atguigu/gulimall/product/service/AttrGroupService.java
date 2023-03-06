package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.vo.AttrAttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrGroupRespVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:43:44
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByCategoryIdAndKey(Map<String, Object> params, Long categoryId);

    PageUtils queryPageByKey(Map<String, Object> params);

    List<AttrEntity> getAttrListByAttrGroupId(Long attrGroupId);

    void deleteRelation(List<AttrAttrGroupRelationVo> relationVos);

    PageUtils noGroupAttrList(Map<String, Object> params, Long catelogId);

    void saveRelation(List<Long> attrIds, Long attrGroupId);

    List<AttrGroupRespVo> listAttrGroupWithAttr(Long catelogId);
}

