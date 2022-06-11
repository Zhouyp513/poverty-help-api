package cn.poverty.interaction.req.user;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.poverty.common.validation.NotEmpty;
import lombok.Data;

import java.io.*;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统用户导入
 * @date 2019-08-13
 */
@Data
public class AuthUserImportReq implements Serializable {


    private static final long serialVersionUID = 8308573347263193216L;

    /**
     * 用户名
     */
    @Excel(name = "用户名" , width = 10)
    @NotEmpty(message = "用户名->不可为空")
    private String userName;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名" , width = 10)
    @NotEmpty(message = "真实姓名->不可为空")
    private String realName;

    /**
     * 手机号
     */
    @Excel(name = "手机号" , width = 10)
    @NotEmpty(message = "手机号->不可为空")
    private String phoneNumber;

    /**
     * 用户锁定状态 1:锁定,2:有效
     */
    @Excel(name = "用户锁定状态" , replace = { "锁定_1", "有效_2"} ,addressList = true,width = 10)
    @NotEmpty(message = "用户锁定状态->不可为空")
    private Integer lockStatus;

    /**
     * 描述
     */
    @Excel(name = "描述" , width = 10)
    @NotEmpty(message = "描述->不可为空")
    private String description;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称", dict = "departmentId", width = 10)
    @NotEmpty(message = "部门名称->不可为空")
    private String departmentId;

    /**
     * 角色名称
     */
    @Excel(name = "角色名称", dict = "authRoleId", width = 10)
    @NotEmpty(message = "角色名称->不可为空")
    private String authRoleId;

    /**
     * 性别 0 :男 1:女,2:保密

     @Excel(name = "性别" , replace = { "大型_01", "中型_02", "小型_03","微型_04"} ,addressList = true,width = 10)
     @NotEmpty(message = "性别->不可为空")
     private Integer gender;*/

}
