package cn.poverty.common.utils.alibaba;

import cn.poverty.common.config.message.MsgCodeConstants;
import cn.poverty.common.constants.BaseConstant;
import com.alibaba.fastjson.JSON;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**

 * @projectName poverty-help-api
 * @Description: 阿里短信发送工具
 * @date 2019-09-16
 */
@Component
@Slf4j
public class AliMessageSendComponent {


    @Resource
    private BaseConstant baseConstant;

    @Resource
    private MsgCodeConstants msgCodeConstants;


    /**
     * 发送阿里云短信
     *
     * @date 2019-09-16
     * @param phone 手机号码
     * @param templateCode 短信模版Code
     * @param signName 签名名称
     * @param variableCodeHashMap 变量值模版
     * @return response
     */
    @Async("threadPoolTaskExecutor")
    public CommonResponse sendAliMessage(String phone,
                                         String templateCode,
                                         String signName,
                                         HashMap<String, String> variableCodeHashMap) throws Exception{

        log.info(">>>>>>>>>>>>>>>>>>>>>执行发送阿里云,当前线程ID:{}<<<<<<<<<<<<<<<<<<<",Thread.currentThread().getId());

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                baseConstant.getAliCloudAccessKeyId(),
                baseConstant.getAliCloudAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(msgCodeConstants.getMessageSendUrl());
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", JSON.toJSONString(variableCodeHashMap));
        CommonResponse response = client.getCommonResponse(request);
        log.info(">>>>>>>>>>>>>>>>>>>>>发送阿里云短信返回:{}<<<<<<<<<<<<<<<<<<<",JSON.toJSONString(response));
        return response;
    }

    /**
     * 发送APP阿里云短信验证码
     *
     * @date 2019-09-16
     * @param phone 手机号码
     * @param messageCode 短信验证码
     * @param signName 签名名称
     * @param templateCode 模版code
     * @return response
     */
    public CommonResponse sendAliMessageVerifyCode(String phone,
                                                   String messageCode,
                                                   String signName,
                                                   String templateCode) throws Exception{
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                baseConstant.getAliCloudAccessKeyId(),
                baseConstant.getAliCloudAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(msgCodeConstants.getMessageSendUrl());
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);

        //短信
        HashMap<Object, Object> templateCodeMap = Maps.newHashMap();

        templateCodeMap.put("code",messageCode);
        request.putQueryParameter("TemplateParam", JSON.toJSONString(templateCodeMap));

        CommonResponse response = client.getCommonResponse(request);

        return response;
    }




}
