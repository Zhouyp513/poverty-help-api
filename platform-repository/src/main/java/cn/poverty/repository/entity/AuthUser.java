package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;
import java.time.Instant;
import java.util.Date;

/**
 * 用户实体
 * @title: AuthUser.java
 * @date 2019/4/24 11:13
 */
@Entity
@Data
@Table(name="auth_user")
public class AuthUser extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 3385765921207673455L;


    /**
     * 用户主键ID
     */
    @Column(name = "auth_user_id",nullable = false)
    private String authUserId = SnowflakeIdWorker.uniqueSequenceStr();

    /**
      * 用户名
      */
    @Column(name = "user_name",unique = true)
    private String userName;


    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 密码
     */
    @Column(name = "auth_password")
    private String authPassword;

    /**
     * 密码盐
     */
    @Column(name = "auth_salt")
    private String authSalt;

    /**
     * 手机号
     */
    @Column(name = "phone_number")
    private String phoneNumber;


    /**
     * 用户锁定状态 1:锁定,2:有效
     */
    @Column(name = "lock_status")
    private Integer lockStatus;


    /**
     * 用户头像
     */
    @Column(name = "avatar")
    private String avatar;


    /**
     * 最后登录时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_login_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime = Date.from(Instant.now());


    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 部门ID
     */
    @Column(name = "department_id")
    private String departmentId;

}
