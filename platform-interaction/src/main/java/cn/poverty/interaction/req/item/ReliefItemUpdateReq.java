package cn.poverty.interaction.req.item;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 
 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫项目更新Req
 * @date 2019-08-12
 */
@Data
public class ReliefItemUpdateReq implements Serializable  {


	private static final long serialVersionUID = -3817166391386711782L;

	/**
	 * 业务主键ID
 	 */
	private String  reliefItemId;

	/**
	 * 项目名称
 	 */
	private String  itemName;

	/**
	 * 项目开始时间
 	 */
	@JSONField(format="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDateTime beginTime;

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
