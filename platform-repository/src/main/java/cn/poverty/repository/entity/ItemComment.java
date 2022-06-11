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
 * @Description: 扶贫项目评论反馈实体
 * @date 2019-08-12
 */
@Table(name = "item_comment")
@Entity
@Data
public class ItemComment extends  BaseEntity implements Serializable  {


	private static final long serialVersionUID = 8235027510606332504L;


	/**
	 * 业务主键ID
 	 */
	@Column(name = "item_comment_id")
	private String  itemCommentId = SnowflakeIdWorker.uniqueSequenceStr();

	/**
	 * 扶贫项目ID
 	 */
	@Column(name = "relief_item_id")
	private String  reliefItemId;

	/**
	 * 意见内容
 	 */
	@Column(name = "comment_content")
	private String  commentContent;

	/**
	 * 反馈人ID
 	 */
	@Column(name = "comment_user_id")
	private String  commentUserId;

	/**
	 * 反馈人
 	 */
	@Column(name = "comment_user_name")
	private String  commentUserName;

	/**
	 * 意见回复
 	 */
	@Column(name = "comment_reply")
	private String  commentReply;

}
