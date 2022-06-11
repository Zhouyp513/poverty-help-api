package cn.poverty.common.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName BaseConstant
 * @Description 基础业务配置
 * @Author
 * @Date 2018年11月16日10:04:29
 * @@Version 1.0
 */
@Data
@Configuration
@Component
public class BaseConstant {


    /**
     * 未删除状态
     */
    @Value("${baseConstant.constant.unDeleteStatus}")
    private Integer unDeleteStatus;

    /**
     * 删除状态
     */
    @Value("${baseConstant.constant.deleteStatus}")
    private Integer deleteStatus;


    /**
     * 系统级别最高权限   1否  2是
     */
    @Value("${baseConstant.constant.originAuthLevel}")
    private Integer originAuthLevel;


    /**
     * 系统级别最高权限   1否  2是
     */
    @Value("${baseConstant.constant.noOriginAuthLevel}")
    private Integer noOriginAuthLevel = 1;


    /**
     * 用户权限缓存前缀
     */
    @Value("${auth.cache-prefix}")
    private String authCachePrefix;


    /**
     * 用户权限缓存时间
     */
    @Value("${auth.expiredTime}")
    private Long authExpiredTime;

    /**
     * 是否打开日志
     */
    @Value("${auth.openAopLog}")
    private Boolean openAopLog;

    /**
     * APP的token前缀
     */
    @Value("${baseConstant.constant.appAuthTokenPrefix}")
    private String appAuthTokenPrefix;

    /**
     * 默认密码
     */
    @Value("${baseConstant.constant.defaultPassword}")
    private String defaultPassword;

    /**
     * 本地IP
     */
    @Value("${baseConstant.constant.localhostIp}")
    private String localhostIp;

    /**
     * 后台系统API阿里云accessKeyId
     */
    @Value("${baseConstant.aliCloud.secretId}")
    private String aliCloudAccessKeyId;

    /**
     * 后台系统API阿里云accessSecret
     */
    @Value("${baseConstant.aliCloud.secretKey}")
    private String aliCloudAccessKeySecret;

    /**
     * 阿里OSS成都区域节点配置
     */
    @Value("${baseConstant.aliCloud.oss.endPoint}")
    private String ossEndPoint;

    /**
     * 阿里OSS成都区bucketName
     */
    @Value("${baseConstant.aliCloud.oss.bucketName}")
    private String ossBucketName;

    /**
     * 文件固定前缀
     */
    @Value("${baseConstant.aliCloud.oss.prefix}")
    private String ossPrefix;

}

