package cn.poverty.interaction.req.item.apply;

import cn.poverty.common.interaction.base.page.BasePageReq;
import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫项目申请Req
 * @date 2019-08-12
 */
@Data
public class ReliefItemApplyPageReq extends BasePageReq implements Serializable  {


	private static final long serialVersionUID = -1157661745750195998L;


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
	 * 是否查询所有
	 */
	private Boolean needAll = false;

	/**
	 * 审核状态 跟随枚举
	 */
	private String  auditStatus;
}
