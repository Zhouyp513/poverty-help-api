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
 * @Description: 追踪记录表
 * @date 2019-08-12
 */
@Table(name = "trace_record")
@Entity
@Data
public class TraceRecord extends BaseEntity implements Serializable  {


	private static final long serialVersionUID = -8807219412131731932L;

	/**
	 * 业务主键ID
 	 */
	@Column(name = "trace_record_id")
	private String  traceRecordId = SnowflakeIdWorker.uniqueSequenceStr();

	/**
	 * 追踪类型
 	 */
	@Column(name = "trace_type")
	private String  traceType;

	/**
	 * 追踪内容
 	 */
	@Column(name = "trace_content")
	private String  traceContent;

}
