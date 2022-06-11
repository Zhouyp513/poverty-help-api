package cn.poverty.interaction.internal.auth;

import cn.poverty.interaction.req.auth.AuthLoginReq;
import lombok.Data;

import java.io.*;

/**
 * @author 
 * @packageName cn.common.interaction.internal.auth
 * @Description: 异步认证所需的基元信息
 * @date 2021-09-30
 */
@Data
public class AsyncAuthMeta implements Serializable {

    private static final long serialVersionUID = 7779450289912702673L;

    /**
      * 用户登录请求
      */
    private AuthLoginReq loginReq;

    /**
     * 用户基元信息
     */
    private AsyncAuthUserMeta authUserResult;

    /**
     * jwtToken
     */
    private AsyncJwtToken jwtToken;

    /**
     * IP地址
     */
    private String ipAddress;

}
