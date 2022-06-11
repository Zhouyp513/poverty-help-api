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
 * @Description: 业务字典实体
 * @date 2019-08-12
 */
@Table(name = "business_dictionary")
@Entity
@Data
public class BusinessDictionary extends BaseEntity implements Serializable  {

	private static final long serialVersionUID = -4608157803549531170L;


	/**
	 * 系统字典主键ID
 	 */
	@Column(name = "business_dictionary_id")
	private String businessDictionaryId = SnowflakeIdWorker.uniqueSequenceStr();

	/**
	 * 业务字典类型
 	 */
	@Column(name = "dict_type")
	private String  dictType;

	/**
	 * 键
 	 */
	@Column(name = "dict_key")
	private String  dictKey;

	/**
	 * 值
 	 */
	@Column(name = "dict_value")
	private String  dictValue;

	/**
	 * 排序字段
	 */
	@Column(name = "sort_index")
	private Integer sortIndex;

	/**
	 * 字典备注
	 */
	@Column(name = "dict_remark")
	private String dictRemark;
}
