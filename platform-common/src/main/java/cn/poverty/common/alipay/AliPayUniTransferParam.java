package cn.poverty.common.alipay;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**

 * @projectName poverty-help-api
 * @Description: 支付宝单笔转账请求参数
 * @date 2020-04-27
 */
@Data
public class AliPayUniTransferParam implements Serializable {

    private static final long serialVersionUID = -3685579514779651548L;


    /**
      * 商户端的唯一订单号，对于同一笔转账请求，商户需保证该订单号唯一。
      */
    @JSONField(name = "out_biz_no")
    private String outBizNo;

    /**
     * 订单总金额，单位为元，精确到小数点后两位，STD_RED_PACKET产品取值范围[0.01,100000000]；
     * TRANS_ACCOUNT_NO_PWD产品取值范围[0.1,100000000]
     */
    @JSONField(name = "trans_amount")
    private BigDecimal transAmount;


    /**
     * 业务产品码，
     * 单笔无密转账到支付宝账户固定为:
     * TRANS_ACCOUNT_NO_PWD；
     * 单笔无密转账到银行卡固定为:
     * TRANS_BANKCARD_NO_PWD;
     * 收发现金红包固定为:
     * STD_RED_PACKET
     */
    @JSONField(name = "product_code")
    private String productCode = "TRANS_ACCOUNT_NO_PWD";


    /**
     * 描述特定的业务场景，可传的参数如下：
     * DIRECT_TRANSFER：单笔无密转账到支付宝/银行卡, B2C现金红包;
     * PERSONAL_COLLECTION：C2C现金红包-领红包
     */
    @JSONField(name = "biz_scene")
    private String bizScene = "DIRECT_TRANSFER";


    /**
     * 转账业务的标题，用于在支付宝用户的账单里显示
     */
    @JSONField(name = "order_title")
    private String orderTitle = "";

    /**
     * 收款方信息
     */
    @JSONField(name = "payee_info")
    private AliPayUniTransferPayee payeeInfo;

}
