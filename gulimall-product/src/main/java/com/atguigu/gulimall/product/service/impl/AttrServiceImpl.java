package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.common.constant.ProductAttrType;
import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.AttrResponVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    private AttrAttrgroupRelationDao relationDao;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private AttrGroupDao attrGroupDao;
    @Resource
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveDetail(AttrVo attrVo) {
        // 保存属性基本信息
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        baseMapper.insert(attrEntity);
        // 保存attr attrGroup 关联关系
        if (attrVo.getAttrType() == 1) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils typeListByCatelogId(String attrType, Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        // 首先拼接attrType
        if (attrType.equals("base")) {
            queryWrapper.eq("attr_type", ProductAttrType.BASE.getCode());
        } else if (attrType.equals("sale")) {
            queryWrapper.eq("attr_type", ProductAttrType.SALE.getCode());
        } else {
            return null;
        }
        // 1.判断params中的检索字段是否为空,不为空拼接查询条件
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.eq("attr_id", key).or().like("attr_name", key);
        }
        // 2.判断catelogId是否为0, 为0查所有
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        // 3.根据分页条件封装成page对象
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        // 4.将page对象中的数据添加上groupName和catelogName
        List<AttrEntity> records = page.getRecords();
        List<AttrResponVo> attrResponVoList = records.stream().map((attrEntity) -> {
            // 1.将attrEntity中的属性复制到attrResponVo
            AttrResponVo attrResponVo = new AttrResponVo();
            BeanUtils.copyProperties(attrEntity, attrResponVo);
            // 2.获取catelogName
            CategoryEntity categoryEntity = categoryDao.selectOne(new QueryWrapper<CategoryEntity>().eq("cat_id", attrResponVo.getCatelogId()));
            if (categoryEntity != null) {
                attrResponVo.setCatelogName(categoryEntity.getName());
            }
            // 3.获取groupName
            if (attrType.equals("base")) {
                AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrResponVo.getAttrId()));
                if (relationEntity != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectOne(new QueryWrapper<AttrGroupEntity>().eq("attr_group_id", relationEntity.getAttrGroupId()));
                    if (attrGroupEntity != null) {
                        attrResponVo.setGroupName(attrGroupEntity.getAttrGroupName());
                    }
                }
            }
            return attrResponVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrResponVoList);
        return pageUtils;
    }

    @Override
    public AttrResponVo getAttrById(Long attrId) {
        // 1.从数据库中获取AttrEntity
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        if (attrEntity == null) {
            return null;
        }
        // 2.将数据拷贝到AttrResponVo
        AttrResponVo attrResponVo = new AttrResponVo();
        BeanUtils.copyProperties(attrEntity, attrResponVo);
        // 3.获取groupName和attrGroupId
        AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrResponVo.getAttrId()));
        if (relationEntity != null) {
            attrResponVo.setAttrGroupId(relationEntity.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
            if (attrGroupEntity != null) {
                attrResponVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }
        // 4.获取catelogName
        CategoryEntity categoryEntity = categoryDao.selectById(attrResponVo.getCatelogId());
        if (categoryEntity != null) {
            attrResponVo.setCatelogName(categoryEntity.getName());
        }
        // 5.获取catelogPath
        Long[] path = categoryService.getPath(attrResponVo.getCatelogId());
        if (path.length > 0) {
            attrResponVo.setCatelogPath(path);
        }
        return attrResponVo;
    }

    @Transactional
    @Override
    public void updateDetail(AttrVo attrVo) {
        // 1.将基本数据保存到attr表中
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        baseMapper.updateById(attrEntity);
        // 2.更新attr - attrGroup 关系表
        if (attrVo.getAttrType() == 1) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attrVo.getAttrId());
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            Long count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrVo.getAttrId()));
            if (count > 0) {
                UpdateWrapper<AttrAttrgroupRelationEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("attr_id", attrVo.getAttrId());
                relationDao.update(relationEntity, updateWrapper);
            } else {
                relationDao.insert(relationEntity);
            }
        }

    }

}