package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * @author
 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫项目申请表
 * @date 2019-08-12
 */
@Table(name = "relief_item_apply")
@Entity
@Data
public class ReliefItemApply extends  BaseEntity implements Serializable  {


	/**
	 * 业务主键ID
 	 */
	@Column(name = "relief_item_apply_id")
	private String  reliefItemApplyId = SnowflakeIdWorker.uniqueSequenceStr();

	/**
	 * 扶贫项目ID
 	 */
	@Column(name = "relief_item_id")
	private String  reliefItemId;

	/**
	 * 申请编号
 	 */
	@Column(name = "apply_no")
	private String  applyNo;

	/**
	 * 申请人ID
	 */
	@Column(name = "apply_user_id")
	private String  applyUserId;

	/**
	 * 申请人姓名
	 */
	@Column(name = "apply_user_name")
	private String  applyUserName;

	/**
	 * 申请理由
 	 */
	@Column(name = "apply_reason")
	private String  applyReason;

	/**
	 * 项目金额
 	 */
	@Column(name = "item_amount")
	private BigDecimal  itemAmount;

	/**
	 * 审核状态 跟随枚举
 	 */
	@Column(name = "audit_status")
	private String  auditStatus;

}
