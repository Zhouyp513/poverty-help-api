package cn.poverty.api;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.WebPlatformApplication;
import cn.poverty.common.config.message.MsgCodeConstants;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.constants.PlatformConstant;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.alibaba.AliMessageSendComponent;
import cn.poverty.repository.repository.AuthDepartmentRepository;
import cn.poverty.repository.repository.AuthUserRepository;
import cn.poverty.repository.repository.BusinessDictionaryRepository;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author
 * @projectName poverty-help-api
 * @Description:
 * @date 2020-06-11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebPlatformApplication.class)
@ActiveProfiles("dev")
@Slf4j
//@Ignore
public class ApiTests {

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthUserRepository authUserRepository;

    @Resource
    private BusinessDictionaryRepository businessDictionaryRepository;

    @Resource
    private AliMessageSendComponent aliMessageSendComponent;

    @Resource
    private PlatformConstant platformConstant;

    @Resource
    private AuthDepartmentRepository authDepartmentRepository;

    @Resource
    private MsgCodeConstants msgCodeConstants;

    @Resource
    private IExcelDictHandler excelDictHandler;

    @Resource
    private MapperFacade mapperFacade;

    /**
      * 测试redis的位置功能
      *
      * @date 2021/7/22
      */
    @Test
    public void testRedisGeo(){

        String s = "祝你考出好成绩！";
        System.out.println(s.length());
    }

}
