package cn.poverty.interaction.resp.user;

import lombok.Data;

import java.io.*;

/**
 * @author
 * @packageName cn.poverty.interaction.resp.user
 * @Description: 系统用户简略信息
 * @date 2021-04-06
 */
@Data
public class AuthUserSketchResp implements Serializable {

    private static final long serialVersionUID = -3145475107507666377L;

    /**
     * 用户主键ID
     */
    private String authUserId;

    /**
     * 用户名
     */
    private String userName;

}
