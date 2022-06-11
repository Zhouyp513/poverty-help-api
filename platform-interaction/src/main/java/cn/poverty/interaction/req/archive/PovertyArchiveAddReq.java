package cn.poverty.interaction.req.archive;

import lombok.Data;

import java.io.*;

/**
 * @author
 * @packageName cn.poverty.repository.entity
 * @Description: 贫困人员档案->交互封装类
 * @date 2019-08-12
 */
@Data
public class PovertyArchiveAddReq implements Serializable  {

	private static final long serialVersionUID = 6729348160828795833L;

	/**
	 * 业务主键ID
 	 */
	private String  povertyArchiveId;

	/**
	 * 帮扶对象ID
 	 */
	private String  archiveUserId;

	/**
	 * 帮扶对象姓名
 	 */
	private String  archiveUserName;

	/**
	 * 帮扶主体
 	 */
	private String  helpMain;

	/**
	 * 结对管理
 	 */
	private String  pairingContent;

}
