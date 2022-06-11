package cn.poverty.interaction.req.trace;

import lombok.Data;

import java.io.*;

/**
 * @author
 * @packageName cn.poverty.repository.entity
 * @Description: 追踪记录->交互封装类
 * @date 2019-08-12
 */
@Data
public class TraceRecordAddReq implements Serializable  {


	private static final long serialVersionUID = 518903744809116907L;

	/**
	 * 业务主键ID
 	 */
	private String  traceRecordId;

	/**
	 * 追踪类型
 	 */
	private String  traceType;

	/**
	 * 追踪内容
 	 */
	private String  traceContent;

}
