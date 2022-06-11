package cn.poverty.interaction.req.auth;

import cn.poverty.common.interaction.base.BaseReq;
import cn.poverty.common.validation.NotEmpty;
import lombok.Data;

import java.io.*;

/**
 * @author
 * @packageName cn.poverty.interaction.req.auth
 * @Description: 登录请求Req
 * @date 2021-06-08
 */
@Data
public class AuthLoginReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 2254455960911546744L;

    /**
      * 用户名
      */
    @NotEmpty(message = "用户名->不可为空")
    private String userName;

    /**
     * 密码
     */
    @NotEmpty(message = "密码->不可为空")
    private String password;

}
