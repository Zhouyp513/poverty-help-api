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
 * @Description: 扶贫救助金评论反馈实体
 * @date 2019-08-12
 */
@Table(name = "fund_comment")
@Entity
@Data
public class FundComment extends BaseEntity implements Serializable  {


	private static final long serialVersionUID = 2682165872242103310L;

	/**
	 * 业务主键ID
 	 */
	@Column(name = "fund_comment_id")
	private String  fundCommentId = SnowflakeIdWorker.uniqueSequenceStr();

	/**
	 * 扶贫救助金ID
 	 */
	@Column(name = "relief_fund_id")
	private String  reliefFundId;

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
