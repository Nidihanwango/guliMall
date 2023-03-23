package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.CategoryBrandRelationDao;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationDao cbrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 从数据库中查询到所有分类信息
        List<CategoryEntity> all = baseMapper.selectList(null);
        // 从所有分类信息中过滤出一级分类
        return all.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0).
                peek(menu -> menu.setChildren(getChildren(menu, all))).
                sorted((menu1, menu2) -> (menu1.getSort() == null? 0 : menu1.getSort()) - (menu2.getSort() == null? 0 : menu2.getSort())).
                collect(Collectors.toList());
    }

    /**
     * 批量逻辑删除
     * @param asList
     */
    @Override
    public void removeMenuByIds(List<Long> asList) {
        // todo 删除前检查是否有菜单引用

        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] getPath(Long catelogId) {
        List<Long> path = new ArrayList<>();
        path.add(catelogId);
        CategoryEntity category = baseMapper.selectById(catelogId);
        getParent(category, path);
        Collections.reverse(path);
        return path.toArray(new Long[0]);
    }

    @Override
    public void updateByIdDetail(CategoryEntity category) {
        // 判断传入的参数中是否包含name属性, 如果包含, 则CategoryBrandRelation表中的category_name也需要更新
        String name = category.getName();
        if (!StringUtils.isEmpty(name)) {
            cbrDao.updateByCatelogId(category.getCatId(), name);
        }
        baseMapper.updateById(category);
    }

    private void getParent(CategoryEntity categoryEntity, List<Long> path) {
        Long parentCid = categoryEntity.getParentCid();
        if (parentCid != 0) {
            path.add(parentCid);
            getParent(baseMapper.selectById(parentCid), path);
        } else {
            return;
        }
    }
    private List<CategoryEntity> getChildren(CategoryEntity categoryEntity, List<CategoryEntity> all) {
        return all.stream().filter(menu -> Objects.equals(menu.getParentCid(), categoryEntity.getCatId())).
                peek(menu -> menu.setChildren(getChildren(menu, all))).
                sorted((menu1, menu2) -> (menu1.getSort() == null? 0 : menu1.getSort()) - (menu2.getSort() == null? 0 : menu2.getSort())).
                collect(Collectors.toList());
    }
}