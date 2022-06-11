package cn.poverty.service.impl;

import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.CriteriaBuilder;
import cn.poverty.common.utils.alibaba.AliOssComponent;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.interaction.helper.PageBuilder;
import cn.poverty.interaction.req.log.SystemApiLogPageReq;
import cn.poverty.interaction.resp.log.SystemApiLogPageQueryResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.repository.entity.SystemApiLog;
import cn.poverty.repository.repository.SystemApiLogRepository;
import cn.poverty.service.AuthUserService;
import cn.poverty.service.LogService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description:
 * @date 2020-01-10
 */
@Service("logService")
@Slf4j
public class LogServiceImpl implements LogService {

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private RedisRepository redisRepository;


    @Resource
    private AliOssComponent aliOssComponent;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private SystemApiLogRepository systemApiLogRepository;


    /**
      * 分页查询系统日志
      *
      * @date 2020-01-10
      * @param systemApiLogPageReq 系统API日志查询请求Req
      * @return Pagination
      */
    @Override
    public Pagination<SystemApiLogPageQueryResp> querySystemApiLogByPage(SystemApiLogPageReq systemApiLogPageReq){

        log.info(">>>>>>>>>>>>>>>>>>分页查询系统日志请求参数<<<<<<<<<<<<<:"+ JSON.toJSONString(systemApiLogPageReq));

        //构建查询条件
        Example systemApiLogExample = new Example(SystemApiLog.class);
        Example.Criteria systemApiLogCriteria = systemApiLogExample.createCriteria();
        CriteriaBuilder.rigidCriteria(systemApiLogCriteria);

        if(!CheckParam.isNull(systemApiLogPageReq.getUserName())){
            systemApiLogCriteria.andLike("userName","%"+systemApiLogPageReq.getUserName()+"%");
        }

        if(!CheckParam.isNull(systemApiLogPageReq.getOperation())){
            systemApiLogCriteria.andLike("operation","%"+systemApiLogPageReq.getOperation()+"%");
        }

        systemApiLogExample.orderBy("createTime").desc();

        //开始分页
        Page<Object> page = PageHelper.startPage(systemApiLogPageReq.getCurrentPage(), systemApiLogPageReq.getItemsPerPage());
        List<SystemApiLog> systemApiLogList = systemApiLogRepository.selectByExample(systemApiLogExample);

        if(CollectionUtils.isEmpty(systemApiLogList)) {
            return PageBuilder.buildPageResult(page,systemApiLogList);
        }

        List<SystemApiLogPageQueryResp> respList = new ArrayList<>();

        systemApiLogList.stream().forEach(u1 -> {
            SystemApiLogPageQueryResp resp = new SystemApiLogPageQueryResp();

            resp.setLocation(u1.getLocation());
            resp.setMethod(u1.getMethod());
            resp.setOperation(u1.getOperation());
            resp.setParams(u1.getParams());
            resp.setRequestIp(u1.getRequestIp());
            resp.setSystemApiLogId(u1.getSystemApiLogId());
            resp.setTime(u1.getTime());
            resp.setUserName(u1.getUserName());
            resp.setCreateTime(u1.getCreateTime());
            respList.add(resp);
        });

        return PageBuilder.buildPageResult(page,respList);
    }


}
