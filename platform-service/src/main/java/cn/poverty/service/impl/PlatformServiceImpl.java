package cn.poverty.service.impl;

import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.utils.common.BaseUtil;
import cn.poverty.common.utils.common.DateTimeUtil;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import cn.poverty.repository.repository.BusinessDictionaryRepository;
import cn.poverty.service.PlatformService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 管理平台操作服务方法实现
 * @date 2020-06-11
 */
@Service(value = "platformService")
@Slf4j
public class PlatformServiceImpl implements PlatformService {

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private BusinessDictionaryRepository businessDictionaryRepository;

    @Resource
    private MapperFacade mapperFacade;

    /**
      * 生成雪花算法ID
      *
      * @date 2021/5/10
      * @return String
      */
    @Override
    public String snowflakeId(){
        return SnowflakeIdWorker.uniqueSequenceStr();
    }

    /**
     * 当前服务器时间
     *
     * @date 2021/5/10
     * @return String
     */
    @Override
    public Long currenLongTime(){
        return DateTimeUtil.currenLongTime();
    }

    /**
     * 从配置文件读取配置编辑器配置信息
     *
     * @date 2021/6/8
     * @return java.util.List
     */
    @Override
    public JSONObject editorConfig(){
        ClassPathResource resource = new ClassPathResource("UEditor/config.json");
        String result = BaseUtil.readClassPathContent(resource);
        if(StrUtil.isEmptyIfStr(result)){
            return new JSONObject();
        }
        return JSON.parseObject(result);
    }
}
