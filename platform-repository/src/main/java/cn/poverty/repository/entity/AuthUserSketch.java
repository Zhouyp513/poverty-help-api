package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;

/**
 * @author
 * @packageName cn.poverty.repository.entity
 * @Description: 系统用户简略信息实体
 * @date 2019-08-12
 */
@Table(name = "auth_user_sketch")
@Entity
@Data
public class AuthUserSketch extends BaseEntity implements Serializable  {


	private static final long serialVersionUID = 8029128115213306675L;


	/**
	 * 业务主键ID
 	 */
	@Column(name = "auth_user_sketch_id")
	private String  authUserSketchId = SnowflakeIdWorker.uniqueSequenceStr();

	/**
	 * 用户业务主键ID
 	 */
	@Column(name = "auth_user_id")
	private String  authUserId;

	/**
	 * 用户简略信息类型 绑定其他的业务ID
	 */
	@Column(name = "sketch_other_id")
	private String sketchOtherId;

	/**
	 * 关联其他表的类型
 	 */
	@Column(name = "sketch_other_type")
	private String sketchOtherType;

}
