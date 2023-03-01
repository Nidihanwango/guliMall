package com.atguigu.gulimall.product.entity;

import com.atguigu.gulimall.common.validator.annotation.HasLength;
import com.atguigu.gulimall.common.validator.group.AddGroup;
import com.atguigu.gulimall.common.validator.group.UpdateGroup;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 属性分组
 * 
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:43:44
 */
@Data
@TableName("pms_attr_group")
public class AttrGroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分组id
	 */
	@TableId
	@NotNull(groups = UpdateGroup.class, message = "属性组ID不能为空")
	private Long attrGroupId;
	/**
	 * 组名
	 */
	@NotBlank(groups = {AddGroup.class}, message = "属性组名不能为空")
	@HasLength(groups = {UpdateGroup.class})
	private String attrGroupName;
	/**
	 * 排序
	 */
	@NotNull(groups = {AddGroup.class}, message = "排序字段不能为空")
	@Min(groups = {AddGroup.class, UpdateGroup.class}, value = 0, message = "排序字段必须是一个大于等于0的数字")
	private Integer sort;
	/**
	 * 描述
	 */
	@NotBlank(groups = {AddGroup.class}, message = "描述不能为空")
	@HasLength(groups = {UpdateGroup.class})
	private String descript;
	/**
	 * 组图标
	 */
	@NotBlank(groups = {AddGroup.class}, message = "组图标不能为空")
	@URL(groups = {AddGroup.class, UpdateGroup.class}, message = "组图标必须为合法URL")
	private String icon;
	/**
	 * 所属分类id
	 */
	@NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "所属分类id不能为空")
	@Min(value = 0, groups = {AddGroup.class, UpdateGroup.class}, message = "所属分类id必须为一个大于等于0的数字")
	private Long catelogId;

	@TableField(exist = false)
	private Long[] catelogPath;
}
