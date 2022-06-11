package cn.poverty.interaction.req.fund.apply;

import cn.poverty.common.validation.NotEmpty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author
 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫资金申请Req
 * @date 2019-08-12
 */
@Data
public class FundApplyReq implements Serializable  {


	private static final long serialVersionUID = 7965101024723662909L;

	/**
	 * 业务主键ID
 	 */
	private String  reliefFundApplyId;

	/**
	 * 扶贫项目ID
 	 */
	@NotEmpty(message = "扶贫项目ID->不可为空")
	private String  reliefFundId;

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

	/**
	 * 民族
	 */
	private String  nation;

	/**
	 * 姓名
	 */
	private String  realName;

	/**
	 * 性别
	 */
	private String  gender;

	/**
	 * 出生日期
	 */
	@JSONField(format="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDateTime birthday;

	/**
	 * 年龄
	 */
	private Integer  age;

	/**
	 * 证件号码
	 */
	private String  idCardNum;

	/**
	 * 电话号码
	 */
	private String  phoneNum;

	/**
	 * 婚姻状况 未婚、已婚、离异、保密
	 */
	private String  maritalStatus;

	/**
	 * 政治面貌
	 */
	private String  politicsStatus;

	/**
	 * 户口类型
	 */
	private String  householdType;

	/**
	 * 籍贯
	 */
	private String  nativePlace;

	/**
	 * 户籍地址
	 */
	private String  nativeAddress;

	/**
	 * 联系地址
	 */
	private String  contactAddress;

	/**
	 * 紧急联系人
	 */
	private String  emergencyContact;

	/**
	 * 紧急联系人与本人关系
	 */
	private String  emergencyRelation;

	/**
	 * 紧急联系人电话
	 */
	private String  emergencyPhone;

	/**
	 * 健康状况
	 */
	private String  healthyCondition;

	/**
	 * 是否有子女
	 */
	private String  childrenStatus;

	/**
	 * 是否有残疾
	 */
	private String  disabilityStatus;

	/**
	 * 是否有低保
	 */
	private String  fundingLowStatus;

	/**
	 * 是否失业
	 */
	private String  unemploymentStatus;

	/**
	 * 职业
	 */
	private String  occupation;

	/**
	 * 工作地址
	 */
	private String  companyAddress;

	/**
	 * 是否为低收入家庭
	 */
	private String  lowIncomeStatus;

	/**
	 * 平均月收入
	 */
	private String  averageMonthlyEarnings;

	/**
	 * 年平均收入
	 */
	private String  averageYearEarnings;

	/**
	 * 附件信息
	 */
	private List<String> attachmentImageList;

}
