package cn.poverty.interaction.req.item;

import cn.poverty.common.interaction.base.data.BaseDataPageReq;
import lombok.Data;

import java.io.*;
import java.math.BigDecimal;

/**
 
 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫项目分页查询Req
 * @date 2019-08-12
 */
@Data
public class ReliefItemPageReq extends BaseDataPageReq implements Serializable  {


	private static final long serialVersionUID = 1503672255583525831L;


	/**
	 * 项目名称
 	 */
	private String  itemName;


	/**
	 * 项目限定人数
 	 */
	private Integer  limitCount;

	/**
	 * 项目金额
 	 */
	private BigDecimal  itemAmount;

	/**
	 * 项目实施地点
 	 */
	private String  itemAddress;

	/**
	 * 项目参加要求
 	 */
	private String  requirements;

	/**
	 * 项目负责人
 	 */
	private String  principal;

}
