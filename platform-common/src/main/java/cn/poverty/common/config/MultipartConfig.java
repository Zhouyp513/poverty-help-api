package cn.poverty.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.io.*;

/**

 * @packageName cn.poverty.common.config
 * @Description: 上传文件的设置
 * @date 2020-12-30
 */
@Configuration
@Slf4j
public class MultipartConfig {


    /**
     * 最大文件大小
     */
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    /**
     * 最大请求大小
     */
    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxRequestSize;

    /**
      * 临时文件目录
      */
    @Value("${locationTemp}")
    private String locationTemp;

    /**
     * 文件上传配置
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize(DataSize.parse(maxFileSize));
        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.parse(maxRequestSize));

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>最大文件大小 : {} , 最大请求大小 : {} <<<<<<<<<<<<<<<<<<<<<<<<<<<<<",maxFileSize,maxRequestSize);

        File patchFile = new File(locationTemp);

        if (!patchFile.exists()) {
            patchFile.mkdirs();
        }

        factory.setLocation(locationTemp);
        return factory.createMultipartConfig();

        }
    }
