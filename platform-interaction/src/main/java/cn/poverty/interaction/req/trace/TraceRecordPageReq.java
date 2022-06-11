package cn.poverty.interaction.req.trace;

import cn.poverty.common.interaction.base.data.BaseDataPageReq;
import lombok.Data;

import java.io.*;

/**
 * @author
 * @packageName cn.poverty.repository.entity
 * @Description: 追踪记录->交互封装类
 * @date 2019-08-12
 */
@Data
public class TraceRecordPageReq extends BaseDataPageReq implements Serializable  {


	private static final long serialVersionUID = 3571636055737893160L;

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
