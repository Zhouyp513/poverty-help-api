package cn.poverty.service.config.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 
 * @packageName cn.poverty.service.config.shiro
 * @Description: 注册后端不需要认证接口url集合
 *               指定auth.shiro开头的变量
 * @date 2021-04-06
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "auth.shiro")
public class AnonUrlListConfiguration {


    /**
      * 后端不需要认证接口url集合
      */
    private List<String> anonUrlList;

}
