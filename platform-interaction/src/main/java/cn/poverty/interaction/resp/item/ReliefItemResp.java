package cn.poverty.interaction.resp.item;

import cn.poverty.common.interaction.base.BaseResp;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author
 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫项目新增Req
 * @date 2019-08-12
 */
@Data
public class ReliefItemResp extends BaseResp implements Serializable  {


	private static final long serialVersionUID = 1503672255583525831L;

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
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
