package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 救助金项目表
 * @title: ReliefFund.java
 * @author
 * @date 2019/4/24 11:13
 */
@Table(name = "relief_fund")
@Entity
@Data
public class ReliefFund extends  BaseEntity implements Serializable  {


	private static final long serialVersionUID = -792653856786338892L;


	/**
	 * 业务主键ID
 	 */
	@Column(name = "relief_fund_id")
	private String  reliefFundId = SnowflakeIdWorker.uniqueSequenceStr();

	/**
	 * 救助金名称
 	 */
	@Column(name = "item_name")
	private String  itemName;

	/**
	 * 救助金金额
	 */
	@Column(name = "fund_amount")
	private BigDecimal fundAmount;

	/**
	 * 救助金类型
 	 */
	@Column(name = "fund_type")
	private String  fundType;

	/**
	 * 救助金等级
 	 */
	@Column(name = "fund_level")
	private String  fundLevel;

	/**
	 * 发放时间
 	 */
	@JSONField(format="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "grant_time")
	private LocalDateTime grantTime;

}
