package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;


/**
 * 权限模块->部门实体
 * @author
 * @title: AuthDepartment.java
 * @date 2019/4/24 11:15
 */
@Entity
@Data
@Table(name="auth_department")
public class AuthDepartment extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 8710483866928580355L;


    /**
     * 部门主键ID
     */
    @Column(name = "department_id",nullable = false)
    private String departmentId = SnowflakeIdWorker.uniqueSequenceStr();


    /**
     * 部门名称
     */
    @Column(name = "department_name",nullable = false)
    private String departmentName;


    /**
     * 上级部门
     */
    @Column(name = "parent_id",nullable = false)
    private String parentId;

    /**
     * 排序
     */
    @Column(name = "order_num",nullable = false)
    private Integer orderNum;


}
