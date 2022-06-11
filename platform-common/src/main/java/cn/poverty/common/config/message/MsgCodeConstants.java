package cn.poverty.common.config.message;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author
 * @packageName cn.poverty.common.config.message
 * @Description: 短信模版配置类
 * @date 2021-05-26
 */
@Component
@ConfigurationProperties(prefix = "base-constant.ali-cloud.message",ignoreUnknownFields = false)
@Data
public class MsgCodeConstants {

    /**
      * 后台系统API阿里短信发送请求地址
      */
    private String messageSendUrl;

    /**
      * 后台系统API阿里短信短信模名称aliMessageTemplateName
      */
    private String messageSignName;

    /**
     * 通知短信模版code-企业开户通知
     */
    private String corpOpenAccCode;

    /**
     * 通知短信模版code-流程审核结果通知
     * ${name}，你提交的类型为${auditType}的流程，当前审核状态为${auditStatus}，请登录系统查看并处理。
     */
    private String auditResultCode;


    /**
     * 通知短信模版code-存在待处理的流程通知
     * ${name}，您当前存在类型为${auditType}的审批流程需要处理，请登录系统查看并处理。
     */
    private String needAuditCode;
}
