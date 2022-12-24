package com.atguigu.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author nidihanwang
 * @email sunlightcs@gmail.com
 * @date 2022-12-24 19:04:45
 */
@Data
@TableName("ums_member_level")
public class MemberLevelEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private Integer growthPoint;
	/**
	 * 
	 */
	private Integer defaultStatus;
	/**
	 * 
	 */
	private BigDecimal freeFreightPoint;
	/**
	 * ÿ
	 */
	private Integer commentGrowthPoint;
	/**
	 * 
	 */
	private Integer priviledgeFreeFreight;
	/**
	 * 
	 */
	private Integer priviledgeMemberPrice;
	/**
	 * 
	 */
	private Integer priviledgeBirthday;
	/**
	 * 
	 */
	private String note;

}
