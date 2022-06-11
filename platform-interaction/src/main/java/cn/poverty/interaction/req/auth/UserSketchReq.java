package cn.poverty.interaction.req.auth;

import cn.poverty.common.enums.auth.data.SketchOtherTypeEnum;
import cn.poverty.common.validation.NotEmpty;
import lombok.Data;

import java.io.*;

/**
 * @author
 * @packageName cn.poverty.interaction.req.auth
 * @Description: 用户简略信息请求
 * @date 2021-05-25
 */
@Data
public class UserSketchReq implements Serializable {


    private static final long serialVersionUID = -7722639909178391290L;


    /**
     * 用户简略信息类型 绑定其他的业务ID
     */
    @NotEmpty(message = "用户简略信息类型-不可为空")
    private String sketchOtherId;

    /**
     * 用户简略信息类型
     */
    @NotEmpty(message = "用户简略信息类型-不可为空")
    private SketchOtherTypeEnum sketchOtherType;
}
