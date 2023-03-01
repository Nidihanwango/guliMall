package com.atguigu.gulimall.product.entity;

import com.atguigu.gulimall.common.validator.annotation.HasLength;
import com.atguigu.gulimall.common.validator.annotation.ListValue;
import com.atguigu.gulimall.common.validator.group.AddGroup;
import com.atguigu.gulimall.common.validator.group.UpdateGroup;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author syh
 * @email 2966188861@qq.com
 * @date 2023-02-12 15:43:44
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@NotNull(groups = UpdateGroup.class)
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(groups = AddGroup.class, message = "品牌名不能为空")
	@HasLength(groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(groups = AddGroup.class, message = "图片路径不能为空")
	@URL(groups = {AddGroup.class, UpdateGroup.class}, message = "图片路径必须是一个合法的url地址")
	private String logo;
	/**
	 * 介绍
	 */
	@NotBlank(groups = AddGroup.class, message = "品牌描述不能为空")
	@HasLength(groups = {AddGroup.class, UpdateGroup.class})
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(groups = AddGroup.class, message = "showStatus不能为空")
	@ListValue(groups = {AddGroup.class, UpdateGroup.class}, values = {0, 1})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(groups = AddGroup.class, message = "检索首字母不能为空")
	@Pattern(groups = {AddGroup.class, UpdateGroup.class}, regexp = "^[a-zA-Z]$", message = "检索首字母必须是一个字母")
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(groups = AddGroup.class, message = "排序字段不能为空")
	@Min(groups = {AddGroup.class, UpdateGroup.class}, value = 0, message = "排序字段必须是一个大于等于0的数字")
	private Integer sort;

}
