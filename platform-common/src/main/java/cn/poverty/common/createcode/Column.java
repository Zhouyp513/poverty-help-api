package cn.poverty.common.createcode;

import lombok.Data;

import java.io.*;

/**
 * 数据库表信息
 * @title: Column.java
 
 * @date 2021-02-14
 */
@Data
public class Column implements Serializable {

	private static final long serialVersionUID = 6613837222412443948L;

	/**
	 * 列名
	 */
	public String columnName;

	/**
	 * 列名描述(注释)
	 */
	public String columnNameDesc;

	/**
	 * 数据类型
	 */
	public String dataType;

	/**
	 * 列长度
	 */
	public Integer columnLength;


	/**
	 * 枚举值
	 */
	public String[] enums;



}
