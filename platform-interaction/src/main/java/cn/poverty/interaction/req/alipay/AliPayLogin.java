package cn.poverty.interaction.req.alipay;

import cn.poverty.common.validation.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 支付宝数据封装
 * @date 2020-06-30
 */
@Data
public class AliPayLogin implements Serializable {

    private static final long serialVersionUID = 2736384517948225784L;


    /**
     * 用户支付宝授权码
     */
    @NotEmpty(message = "用户支付宝授权码->不可为空")
    private String authCode;

}
