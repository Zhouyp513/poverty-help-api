package cn.poverty.common.alipay;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 
 * @projectName poverty-help-api
 * @Description: 支付宝单笔转账->收款方信息
 * @date 2020-04-27
 */
@Data
public class AliPayUniTransferPayee implements Serializable {


    private static final long serialVersionUID = -7927637064423008655L;




    /**
     * 参与方的唯一标识
     */
    @JSONField(name = "identity")
    private String identity;

    /**
     * 参与方的标识类型，目前支持如下类型：
     * 1、ALIPAY_USER_ID 支付宝的会员ID
     * 2、ALIPAY_LOGON_ID：支付宝登录号，支持邮箱和手机号格式
     */
    @JSONField(name = "identity_type")
    private String identityType = "ALIPAY_USER_ID";


    /**
     *  参与方真实姓名，如果非空，将校验收款支付宝账号姓名一致性。当identity_type=ALIPAY_LOGON_ID时，本字段必填。
     */
    private String name;




}
