package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author
 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫资金申请表
 * @date 2019-08-12
 */
@Table(name = "relief_fund_apply")
@Entity
@Data
public class ReliefFundApply extends  BaseEntity implements Serializable  {


	private static final long serialVersionUID = 2519661257207460756L;


	/**
	 * 业务主键ID
 	 */
	@Column(name = "relief_fund_apply_id")
	private String  reliefFundApplyId = SnowflakeIdWorker.uniqueSequenceStr();

	/**
	 * 扶贫项目ID
 	 */
	@Column(name = "relief_fund_id")
	private String  reliefFundId;

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

	/**
	 * 姓名
	 */
	@Column(name = "real_name")
	private String  realName;

	/**
	 * 民族
	 */
	@Column(name = "nation")
	private String  nation;

	/**
	 * 性别
	 */
	@Column(name = "gender")
	private String  gender;

	/**
	 * 出生日期
	 */
	@JSONField(format="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "birthday")
	private LocalDateTime birthday = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());

	/**
	 * 年龄
	 */
	@Column(name = "age")
	private Integer  age;

	/**
	 * 证件号码
	 */
	@Column(name = "id_card_num")
	private String  idCardNum;

	/**
	 * 电话号码
	 */
	@Column(name = "phone_num")
	private String  phoneNum;

	/**
	 * 婚姻状况 未婚、已婚、离异、保密
	 */
	@Column(name = "marital_status")
	private String  maritalStatus;

	/**
	 * 政治面貌
	 */
	@Column(name = "politics_status")
	private String  politicsStatus;

	/**
	 * 户口类型
	 */
	@Column(name = "household_type")
	private String  householdType;

	/**
	 * 籍贯
	 */
	@Column(name = "native_place")
	private String  nativePlace;

	/**
	 * 户籍地址
	 */
	@Column(name = "native_address")
	private String  nativeAddress;

	/**
	 * 联系地址
	 */
	@Column(name = "contact_address")
	private String  contactAddress;

	/**
	 * 紧急联系人
	 */
	@Column(name = "emergency_contact")
	private String  emergencyContact;

	/**
	 * 紧急联系人与本人关系
	 */
	@Column(name = "emergency_relation")
	private String  emergencyRelation;

	/**
	 * 紧急联系人电话
	 */
	@Column(name = "emergency_phone")
	private String  emergencyPhone;

	/**
	 * 健康状况
	 */
	@Column(name = "healthy_condition")
	private String  healthyCondition;

	/**
	 * 是否有子女
	 */
	@Column(name = "children_status")
	private String  childrenStatus;

	/**
	 * 是否有残疾
	 */
	@Column(name = "disability_status")
	private String  disabilityStatus;

	/**
	 * 是否有低保
	 */
	@Column(name = "funding_low_status")
	private String  fundingLowStatus;

	/**
	 * 是否失业
	 */
	@Column(name = "unemployment_status")
	private String  unemploymentStatus;

	/**
	 * 职业
	 */
	@Column(name = "occupation")
	private String  occupation;

	/**
	 * 工作地址
	 */
	@Column(name = "company_address")
	private String  companyAddress;

	/**
	 * 是否为低收入家庭
	 */
	@Column(name = "low_income_status")
	private String  lowIncomeStatus;

	/**
	 * 平均月收入
	 */
	@Column(name = "average_monthly_earnings")
	private String  averageMonthlyEarnings;

	/**
	 * 年平均收入
	 */
	@Column(name = "average_year_earnings")
	private String  averageYearEarnings;



}
