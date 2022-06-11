package cn.poverty.common.config.druid;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author
 * @packageName cn.poverty.common.config
 * @Description:
 * @date 2021-10-18
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidInitSqlConfiguration {

    /**
      * 连接初始化SQL
      */
    private List<String> connectionInitSQLs;
}
