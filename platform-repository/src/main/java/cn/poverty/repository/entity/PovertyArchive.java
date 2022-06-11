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
 * @Description: 贫困人员档案表
 * @date 2019-08-12
 */
@Table(name = "poverty_archive")
@Entity
@Data
public class PovertyArchive extends BaseEntity implements Serializable  {


	private static final long serialVersionUID = -1301183448603975539L;

	/**
	 * 业务主键ID
 	 */
	@Column(name = "poverty_archive_id")
	private String  povertyArchiveId = SnowflakeIdWorker.uniqueSequenceStr();

	/**
	 * 帮扶对象ID
 	 */
	@Column(name = "archive_user_id")
	private String  archiveUserId;

	/**
	 * 帮扶对象姓名
 	 */
	@Column(name = "archive_user_name")
	private String  archiveUserName;

	/**
	 * 帮扶主体
 	 */
	@Column(name = "help_main")
	private String  helpMain;

	/**
	 * 结对管理
 	 */
	@Column(name = "pairing_content")
	private String  pairingContent;

}
