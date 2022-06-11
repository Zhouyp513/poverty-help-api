package cn.poverty.interaction.req.alipay;

import cn.poverty.common.validation.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 支付宝签名请求封装
 * @date 2020-06-30
 */
@Data
public class AliPaySignReq implements Serializable {

    private static final long serialVersionUID = 1500825439747544110L;




    /**
      * 需要签名的字符串needSignContent
      */
    @NotEmpty(message = "需要签名的字符串needSignContent->不可为空")
    private String needSignContent;


    /**
     * SDK的版本号
     */
    @NotEmpty(message = "SDK的版本号->不可为空")
    private String sdkVersion;

}
