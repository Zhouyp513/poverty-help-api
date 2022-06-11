/*
package poverty.common.config.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

*/
/**
 * Fegin的Header支持配置
 
 * @time 2018/10/10
 * @description
 *//*

@Configuration
public class FeginHeaderSupporter {

    @Value("${sign.key}")
    private String key;

    */
/**
     * 返回请求拦截器
     * @return
     *//*

    @Bean
    public RequestInterceptor getRequestInterceptor(){
        return new RequestHeaderInterceptor(key);
    }

}
*/
