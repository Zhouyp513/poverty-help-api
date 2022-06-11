package cn.poverty.common.interaction;

import cn.poverty.common.validation.NotEmpty;
import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poverty.common.interaction
 * @Description: 微信页面二维码生成请求参数封装
 * @date 2021-02-26
 */
@Data
public class WxPageQrCode implements Serializable {



    /**
      * 微信页面二维码所带的参数
      */
    @NotEmpty(message = "微信页面二维码所带的参数->不可为空")
    private String scene;

    /**
     * 微信页面路径
     */
    @NotEmpty(message = "微信页面路径->不可为空")
    private String page;

    /**
     * 物品ID
     */
    @NotEmpty(message = "物品ID->不可为空")
    private String itemId;

}
