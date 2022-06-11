package cn.poverty.interaction.req.user;

import lombok.Data;

import javax.persistence.Column;
import java.io.*;

/**
 
 * @packageName cn.poverty.interaction.req.user
 * @Description: 用户资料编辑请求Req
 * @date 2021-04-17
 */
@Data
public class UserProfileUpdateReq implements Serializable {

    private static final long serialVersionUID = 3209275766938741513L;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 用户主键ID
     */
    private String authUserId;

    /**
     * 描述
     */
    private String description;

    /**
     * 用户头像
     */
    private String avatar;

}
