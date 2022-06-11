package cn.poverty.common.config.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
  * 对象存储配置类
  * 
  * @date 2020/12/29
  */
@Configuration
@EnableConfigurationProperties({OssConstants.class})
@Component
public class ObjectStorage {


    /**
     *
     */
    @Resource
    private OssConstants oss;

    /**
     * 初始化OSS客户端
     * @throws Exception
     */
    public OSS ossClient() throws Exception {

        //初始化OSS客户端
        OSS ossClient = new OSSClientBuilder().build(oss.getEndPoint(), oss.getSecretId(), oss.getSecretKey());

        String bucket = oss.getBucketName();

        boolean bucketExistFlag = ossClient.doesBucketExist(bucket);
        if (!bucketExistFlag) {
            throw new Exception("指定的bucket: " + bucket + " 不存在");
        }

        return ossClient;
    }
}
