package cn.poverty.common.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 
 * @Title: PageHelperConfig
 * @ProjectName poverty-help-api
 * @Description: 分页插件配置
 * @date 2018/11/2 10:25
 */
@Configuration
public class PageHelperConfig {

    /**
     * 注入PageHelper配置
     * 
     * @date 2021/3/30
     * @return PageHelper
     */
    @Bean
    public PageHelper getPageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        //reasonable 此参数为分页参数合理化 启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页
        //禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据。
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
