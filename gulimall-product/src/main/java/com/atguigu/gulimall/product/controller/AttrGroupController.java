package com.atguigu.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.atguigu.gulimall.common.validator.group.AddGroup;
import com.atguigu.gulimall.common.validator.group.UpdateGroup;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.AttrAttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrGroupRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.R;



/**
 * 属性分组
 *
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:43:44
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;
    /**
     * 列表
     */
    @RequestMapping("/list")
//   @RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPageByKey(params);

        return R.ok().put("page", page);
    }
    @RequestMapping("/list/{categoryId}")
//   @RequiresPermissions("product:attrgroup:list")
    public R listByCategoryId(@RequestParam Map<String, Object> params, @PathVariable Long categoryId){
        PageUtils page = attrGroupService.queryPageByCategoryIdAndKey(params, categoryId);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
//    @RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long[] path = categoryService.getPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }
    @RequestMapping("/attrlist/{attrGroupId}")
    public R attrList(@PathVariable("attrGroupId") Long attrGroupId){
        List<AttrEntity> AttrList = attrGroupService.getAttrListByAttrGroupId(attrGroupId);

        return R.ok().put("data", AttrList);
    }
    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:attrgroup:save")
    public R save(@Validated(value = AddGroup.class) @RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:attrgroup:update")
    public R update(@Validated(value = UpdateGroup.class) @RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }
    @RequestMapping("/deleteRelation")
//    @RequiresPermissions("product:attrgroup:delete")
    public R deleteRelation(@RequestBody List<AttrAttrGroupRelationVo> relationVos){
        attrGroupService.deleteRelation(relationVos);

        return R.ok();
    }
    @GetMapping("/noGroupAttrList/{catelogId}")
    public R noGroupAttrList(@RequestParam Map<String, Object> params, @PathVariable Long catelogId){
        PageUtils page = attrGroupService.noGroupAttrList(params, catelogId);

        return R.ok().put("page", page);
    }

    @PostMapping("/saveRelation/{attrGroupId}")
    public R saveRelation(@RequestBody List<Long> AttrIds, @PathVariable Long attrGroupId){
        attrGroupService.saveRelation(AttrIds, attrGroupId);

        return R.ok();
    }

    @GetMapping("/{catelogId}/withattr")
    public R listAttrGroupWithAttr(@PathVariable Long catelogId){
        List<AttrGroupRespVo> data = attrGroupService.listAttrGroupWithAttr(catelogId);

        return R.ok().put("data", data);
    }
}
