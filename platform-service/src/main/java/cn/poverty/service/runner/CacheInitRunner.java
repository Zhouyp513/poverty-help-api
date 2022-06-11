package cn.poverty.service.runner;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 缓存初始化
 * @date 2019-08-21
 */
@Slf4j
@Component
public class CacheInitRunner implements ApplicationRunner {



    /**
      * 当前Runner的执行方法
      *
      * @date 2019-08-21
      * @param applicationArguments 当前应用启动参数
      */
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>当前应用启动 {} <<<<<<<<<<<<<:", JSON.toJSONString(applicationArguments));
    }
}
