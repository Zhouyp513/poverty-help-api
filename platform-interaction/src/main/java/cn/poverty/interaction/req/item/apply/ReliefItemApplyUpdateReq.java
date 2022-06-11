package cn.poverty.interaction.req.item.apply;

import lombok.Data;

import java.io.*;
import java.math.BigDecimal;

/**
 
 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫项目申请Req
 * @date 2019-08-12
 */
@Data
public class ReliefItemApplyUpdateReq implements Serializable  {


	private static final long serialVersionUID = -1784821664589579270L;


	/**
	 * 业务主键ID
 	 */
	private String  reliefItemApplyId;

	/**
	 * 扶贫项目ID
 	 */
	private String  reliefItemId;

	/**
	 * 申请编号
 	 */
	private String  applyNo;

	/**
	 * 申请人ID
	 */
	private String  applyUserId;

	/**
	 * 申请人姓名
	 */
	private String  applyUserName;

	/**
	 * 申请理由
 	 */
	private String  applyReason;

	/**
	 * 项目金额
 	 */
	private BigDecimal  itemAmount;

	/**
	 * 审核状态 跟随枚举
 	 */
	private String  auditStatus;

}
