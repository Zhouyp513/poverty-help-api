package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;

/**
 * 角色实体
 * @title: AuthRole.java
 * @author
 * @date 2019/4/24 11:13
 * @return
 */
@Entity
@Table(name = "auth_role")
@Data
public class AuthRole extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -8270859731558299187L;


    /**
     * 系统角色主键ID
     */
    @Column(name = "auth_role_id",nullable = false)
    private String authRoleId = SnowflakeIdWorker.uniqueSequenceStr();

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 角色代码
     */
    @Column(name = "role_code")
    private String roleCode;


    /**
     * 是否可选 01 是 02 否
     */
    @Column(name = "optional_status")
    private String optionalStatus;


    /**
     * 角色描述
     */
    @Column(name = "remark")
    private String remark;


}
