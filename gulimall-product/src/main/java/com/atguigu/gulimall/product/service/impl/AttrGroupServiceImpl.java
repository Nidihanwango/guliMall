package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrService;
import com.atguigu.gulimall.product.vo.AttrAttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrGroupRespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Resource
    private AttrAttrgroupRelationDao relationDao;
    @Resource
    private AttrService attrService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCategoryIdAndKey(Map<String, Object> params, Long categoryId) {
        if (categoryId == 0) {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<AttrGroupEntity>()
            );
            return new PageUtils(page);
        } else {
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", categoryId);
            String key = (String) params.get("key");
            if (!StringUtils.isEmpty(key)) {
                wrapper.and((obj) -> {
                    obj.eq("attr_group_id", key).or().like("attr_group_name", key).or().like("descript", key);
                });
            }
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }
    }

    @Override
    public PageUtils queryPageByKey(Map<String, Object> params) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        // 查看params中的key是否为空, 不为空则拼接查询条件
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper = new QueryWrapper<>();
            wrapper.eq("attr_group_id", key).or().like("attr_group_name", key).or().like("descript", key);
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> getAttrListByAttrGroupId(Long attrGroupId) {
        if (attrGroupId == 0) {
            return null;
        }
        // 1.从关系表中根据attrGroupId查出attrIds
        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_group_id", attrGroupId);
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(queryWrapper);
        // 2.从属性表中查出属性名和可选值
        List<Long> ids = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        return attrService.listByIds(ids);
    }

    @Override
    public void deleteRelation(List<AttrAttrGroupRelationVo> relationVos) {
        if (relationVos != null && relationVos.size() > 0) {
            relationDao.deleteBatchByVos(relationVos);
        }
    }

    @Override
    public PageUtils noGroupAttrList(Map<String, Object> params, Long catelogId) {
        // 1.查询所有属性
        // 创建QueryWrapper
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        // 判断param中的检索条件是否为空, 拼接查询条件
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.like("attr_name", key);
        }
        // 根据catelogId查询attr
        if (catelogId != null && catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        } else {
            return null;
        }
        List<AttrEntity> attrList = attrService.list(queryWrapper);
        CopyOnWriteArrayList<AttrEntity> safeAttrList = new CopyOnWriteArrayList<>(attrList);
        // 2.去除已经进行分组的属性
        List<AttrAttrgroupRelationEntity> relationList = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>());
        // 将relationList的属性id放到set中
        Set<Long> set = new HashSet<>();
        relationList.forEach(item -> {
            set.add(item.getAttrId());
        });
        safeAttrList.forEach(item -> {
            if (set.contains(item.getAttrId())){
                safeAttrList.remove(item);
            }
        });
        // 3.封装成pageutils对象返回
        // 获取param中的pagesize
        int limit = Integer.parseInt((String) params.get("limit"));
        int page = Integer.parseInt((String) params.get("page"));
        return new PageUtils(safeAttrList, safeAttrList.size(), limit, page);
    }

    @Override
    public void saveRelation(List<Long> attrIds, Long attrGroupId) {
        if (attrGroupId == null || attrGroupId == 0 || attrIds == null || attrIds.size() == 0) {
            return;
        }

        relationDao.insertBatch(attrIds, attrGroupId);
    }

    @Override
    public List<AttrGroupRespVo> listAttrGroupWithAttr(Long catelogId) {
        // 1.校验参数
        if (catelogId == null || catelogId == 0) {
            return null;
        }
        // 2.根据catelogId获取分类下所有属性分组
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id", catelogId);
        List<AttrGroupEntity> attrGroupEntityList = this.list(queryWrapper);
        if (attrGroupEntityList == null || attrGroupEntityList.isEmpty()) {
            return null;
        }
        // 2.1根据catelogId获取分类下所有属性
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId);
        List<AttrEntity> attrList = attrService.list(attrEntityQueryWrapper);
        if (attrList == null || attrList.isEmpty()) {
            return null;
        }
        // 2.1.1将所有属性按照<id: AttrEntity>存入map中
        Map<Long, AttrEntity> map = new HashMap<>();
        attrList.forEach(item -> {
            map.put(item.getAttrId(), item);
        });
        // 3.将属性分组的类型转换为AttrGroupRespVo
        CopyOnWriteArrayList<AttrGroupRespVo> result = attrGroupEntityList.stream().map(item -> {
            AttrGroupRespVo attrGroupRespVo = new AttrGroupRespVo();
            BeanUtils.copyProperties(item, attrGroupRespVo);
            return attrGroupRespVo;
        }).collect(Collectors.toCollection(CopyOnWriteArrayList::new));
        // 3.根据属性分组id获取分组下所有属性 from 属性分组和属性关系表
        result.forEach(item -> {
            Long attrGroupId = item.getAttrGroupId();
            List<Long> attrIds = relationDao.listAttrIdsByGroupId(attrGroupId);
            attrIds.forEach(id -> {
                AttrEntity attrEntity = map.get(id);
                if (attrEntity != null) {
                    item.getAttrs().add(attrEntity);
                }
            });
        });
        return result;
    }

}