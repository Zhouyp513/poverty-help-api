package cn.poverty.common.interaction.wx;

import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poverty.common.interaction.req.app
 * @Description: 微信的基础请求信息
 * @date 2021-02-25
 */
@Data
public class WxBaseReq implements Serializable {

    private static final long serialVersionUID = -6176505282936345088L;

    /**
     * 微信小程序登陆的code
     */
    private String code;

    /**
     * 微信的授权类型
     */
    private String grantType;
}
