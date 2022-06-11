package cn.poverty.repository.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.*;
import java.util.Date;

/**
 * @author
 * @projectName poverty-help-api
 * @Description:
 * @date 2019-08-20
 */
@Data
public class AuthUserResult implements Serializable {


    private static final long serialVersionUID = 6977387030150824445L;

    /**
     * 用户ID
     */
    private String id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色ID
     */
    private String authRoleId;

    /**
     * 角色代码
     */
    private String roleCode;


    /**
     * 用户主键ID
     */
    private String authUserId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 密码
     */
    private String authPassword;

    /**
     * 密码盐
     */
    private String authSalt;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 用户锁定状态 1:锁定,2:有效
     */
    private Integer lockStatus;

    /**
     * 性别 0:男1:女,2:保密
     */
    private Integer gender;

    /**
     * 用户头像
     */
    private String avatar;


    /**
     * 最后登录时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 描述
     */
    private String description;


    /**
     * 部门名称
     */
    private String departmentName;

}
