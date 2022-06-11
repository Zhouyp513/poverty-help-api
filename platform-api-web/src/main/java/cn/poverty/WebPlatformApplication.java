package cn.poverty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启动入口类
 * @author
 * @packageName cn.poverty
 * @Description: 主启动类
 * @date 2021-01-21 此类默认扫描当前包及其子包 所以在poverty
 */
@MapperScan(basePackages = "cn.poverty.repository.repository")
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
@Configuration
@SpringBootApplication
@EnableTransactionManagement
@Slf4j
public class WebPlatformApplication {

    /*@EnableAutoConfiguration
    @EnableCaching
    @SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
     此处应该使用tk.mybatis.spring.annotation.MapperScan*/

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(WebPlatformApplication.class);
        springApplication.run(args);
    }
}
