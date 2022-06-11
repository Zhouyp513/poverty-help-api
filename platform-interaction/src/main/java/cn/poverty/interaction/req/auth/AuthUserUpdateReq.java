package cn.poverty.interaction.req.auth;

import cn.poverty.common.validation.NotEmpty;
import lombok.Data;

import java.io.*;
import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 后台用户更新Req
 * @date 2019-08-31
 */
@Data
public class AuthUserUpdateReq implements Serializable {

    private static final long serialVersionUID = -874641595827859526L;

    /**
     * 业务主键ID
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String authUserId ;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 密码 最少六位

    @Length(min = 6,message = "用户密码最少6位")
    private String password;  */

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
     * 角色ID
     */
     private String authRoleId;

    /**
     * 用户简略信息集合
     */
    private List<UserSketchReq> sketchList;

}
