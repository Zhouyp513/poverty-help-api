package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author
 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫项目表
 * @date 2019-08-12
 */
@Table(name = "relief_item")
@Entity
@Data
public class ReliefItem extends  BaseEntity implements Serializable  {


	private static final long serialVersionUID = -4687910251351972612L;


	/**
	 * 业务主键ID
 	 */
	@Column(name = "relief_item_id")
	private String  reliefItemId = SnowflakeIdWorker.uniqueSequenceStr();

	/**
	 * 项目名称
 	 */
	@Column(name = "item_name")
	private String  itemName;

	/**
	 * 项目开始时间
 	 */
	@JSONField(format="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "begin_time")
	private LocalDateTime beginTime;

	/**
	 * 项目限定人数
 	 */
	@Column(name = "limit_count")
	private Integer  limitCount;

	/**
	 * 项目金额
 	 */
	@Column(name = "item_amount")
	private BigDecimal  itemAmount;

	/**
	 * 项目实施地点
 	 */
	@Column(name = "item_address")
	private String  itemAddress;

	/**
	 * 项目参加要求
 	 */
	@Column(name = "requirements")
	private String  requirements;

	/**
	 * 项目负责人
 	 */
	@Column(name = "principal")
	private String  principal;

}
