package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.vo.AttrAttrGroupRelationVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:43:44
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBatchByVos(@Param("relationVos") List<AttrAttrGroupRelationVo> relationVos);

    void insertBatch(@Param("attrIds") List<Long> attrIds, @Param("attrGroupId") Long attrGroupId);

    List<Long> listAttrIdsByGroupId(@Param("groupId") Long groupId);
}
