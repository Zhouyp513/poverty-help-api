package cn.poverty.interaction.resp.item.comment;

import cn.poverty.common.interaction.base.BaseResp;
import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poverty.repository.entity
 * @Description: 扶贫项目评论反馈->交互封装类
 * @date 2019-08-12
 */
@Data
public class ItemCommentResp extends BaseResp implements Serializable  {


	private static final long serialVersionUID = 8542859783187506953L;

	/**
	 * 业务主键ID
	 */
	private String  itemCommentId;

	/**
	 * 扶贫项目ID
	 */
	private String  reliefItemId;

	/**
	 * 项目名称
	 */
	private String  itemName;

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
