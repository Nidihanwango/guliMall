package com.atguigu.gulimall.product.vo;

import lombok.Data;

@Data
public class AttrAttrGroupRelationVo {

    private Long id;
    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性分组id
     */
    private Long attrGroupId;
    /**
     * 属性组内排序
     */
    private Integer attrSort;
}
