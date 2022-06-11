package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;

/**
 * 权限实体
 * @title: AuthPermission.java
 * @author
 * @date 2019/4/24 11:13
 * @return
 */
@Entity
@Data
@Table(name = "permission")
public class AuthPermission extends BaseEntity implements Serializable{


	private static final long serialVersionUID = -39684444587881329L;

	/**
	  * 权限名称
	  */
	@Column(name = "name",nullable = false)
	private String name;

	/**
	 * 上级类目
	 */
	@Column(name = "pid",nullable = false)
	private Long pid;

	/**
	  * 权限别名
	  */
	@Column(name = "alias",nullable = false)
	private String alias;

}
