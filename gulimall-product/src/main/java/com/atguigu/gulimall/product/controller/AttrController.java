package com.atguigu.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;
import com.atguigu.gulimall.product.service.ProductAttrValueService;
import com.atguigu.gulimall.product.vo.AttrResponVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.R;

import javax.annotation.Resource;


/**
 * 商品属性
 *
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:43:44
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Resource
    private ProductAttrValueService productAttrValueService;

    /**
     * 列表
     */
    @RequestMapping("/{attrType}/list/{catelogId}")
//   @RequiresPermissions("product:attr:list")
    public R list(@PathVariable("attrType") String attrType, @RequestParam Map<String, Object> params, @PathVariable Long catelogId){
        PageUtils page = attrService.typeListByCatelogId(attrType, params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
//    @RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrResponVo attrResponVo = attrService.getAttrById(attrId);

        return R.ok().put("attr", attrResponVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attrVo){
		attrService.saveDetail(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attrVo){
		attrService.updateDetail(attrVo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

    // /product/attr/base/listforspu/{spuId}
    @GetMapping("/base/listforspu/{spuId}")
    public R listForSpu(@PathVariable Long spuId) {
        List<ProductAttrValueEntity> data = productAttrValueService.listForSpu(spuId);

        return R.ok().put("data", data);
    }

    ///product/attr/update/{spuId}
    @PostMapping("/update/{spuId}")
    public R updateBySpuId(@PathVariable Long spuId, @RequestBody List<ProductAttrValueEntity> list) {
        productAttrValueService.updateBySpuId(spuId, list);

        return R.ok();
    }
}
