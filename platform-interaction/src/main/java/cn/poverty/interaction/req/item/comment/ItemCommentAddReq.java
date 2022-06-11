package cn.poverty.interaction.req.item.comment;

import lombok.Data;

import java.io.*;

/**

 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫项目评论反馈->交互封装类
 * @date 2019-08-12
 */
@Data
public class ItemCommentAddReq implements Serializable  {


	private static final long serialVersionUID = 103243975441089703L;


	/**
	 * 业务主键ID
	 */
	private String  itemCommentId;

	/**
	 * 扶贫项目ID
	 */
	private String  reliefItemId;

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
