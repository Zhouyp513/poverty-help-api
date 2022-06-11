package cn.poverty.interaction.req.fund.comment;

import cn.poverty.common.interaction.base.data.BaseDataPageReq;
import lombok.Data;

import java.io.*;

/**
 * @author
 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫救助金评论反馈->交互封装类
 * @date 2019-08-12
 */
@Data
public class FundCommentPageReq extends BaseDataPageReq implements Serializable  {


	private static final long serialVersionUID = 7935833548005623452L;

	/**
	 * 业务主键ID
 	 */
	private String  fundCommentId;

	/**
	 * 扶贫救助金ID
 	 */
	private String  reliefFundId;

	/**
	 * 意见内容
 	 */
	private String  commentContent;

	/**
	 * 反馈人ID
 	 */
	private String  commentUserId;

	/**
	 * 反馈人
 	 */
	private String  commentUserName;

	/**
	 * 意见回复
 	 */
	private String  commentReply;

}
