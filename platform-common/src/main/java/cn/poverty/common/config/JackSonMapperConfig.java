package cn.poverty.common.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用JackSonMapper
 
 * @time 2018/9/29
 * @description 验证器 基本的控制器
 */
@Configuration
public class JackSonMapperConfig {


    @Bean
    public FastJsonHttpMessageConverter objectMapper(){
        //创建转换对象
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

        //创建配置文件对象
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.PrettyFormat,
                //注释掉这两个，前端就可以显示出菜单，包括子菜单等
                //SerializerFeature.WriteNullListAsEmpty,
                //SerializerFeature.WriteMapNullValue,
                SerializerFeature.NotWriteDefaultValue,
                SerializerFeature.WriteNullStringAsEmpty,
                /*SerializerFeature.WriteNullNumberAsZero,*/
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat

        );
        fastJsonHttpMessageConverter.setFastJsonConfig(config);

        // 解决乱码的问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        return fastJsonHttpMessageConverter;
    }

}
