package cn.poverty.service.impl;

import cn.poverty.common.constants.AuthConstants;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.enums.auth.AuthUserLockStatus;
import cn.poverty.common.enums.auth.data.AuthUserOtherTypeEnum;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.common.DateTimeUtil;
import cn.poverty.interaction.internal.auth.ActiveUser;
import cn.poverty.interaction.internal.auth.AsyncAuthMeta;
import cn.poverty.interaction.internal.auth.AsyncAuthUserMeta;
import cn.poverty.interaction.internal.auth.AsyncJwtToken;
import cn.poverty.interaction.internal.auth.AuthUserMeta;
import cn.poverty.interaction.req.auth.AuthLoginReq;
import cn.poverty.interaction.req.auth.AuthUserAddReq;
import cn.poverty.interaction.req.user.AuthUserImportReq;
import cn.poverty.repository.entity.AuthUserSketch;
import cn.poverty.repository.entity.SystemLoginLog;
import cn.poverty.repository.repository.AuthUserRepository;
import cn.poverty.repository.repository.AuthUserSketchRepository;
import cn.poverty.repository.repository.SystemLoginLogRepository;
import cn.poverty.service.AsyncHandlerService;
import cn.hutool.core.util.StrUtil;
import cn.poverty.service.AuthUserService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.compress.utils.Lists;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**

 * @packageName cn.poverty.service.impl
 * @Description: 异步处理服务方法实现
 * @date 2021-09-30
 */
@Service(value = "asyncHandlerService")
@Slf4j
public class AsyncHandlerServiceImpl implements AsyncHandlerService {

    @Resource
    private AuthUserRepository authUserRepository;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private SystemLoginLogRepository systemLoginLogRepository;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private AuthUserSketchRepository authUserSketchRepository;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private HttpServletRequest request;


    /**
      * 导入系统用户
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2022/1/7
      * @param importList 导入数据
      */
    @Override
    @Async("threadPoolTaskExecutor")
    public void importUser(List<AuthUserImportReq> importList){
        log.info(">>>>>>>>>>>异步处理导入系统用户信息参数authUserId:{}<<<<<<<<<<<",JSON.toJSONString(importList));
        log.info(">>>>>>>>>>>异步处理导入系统用户信息线程ID和名称:{},{}<<<<<<<<<<<",
                Thread.currentThread().getId(),
                Thread.currentThread().getName());
        if(CollectionUtils.isEmpty(importList)){
            return;
        }
        importList.stream().forEach(item -> {
            AuthUserAddReq newUser = new AuthUserAddReq();
            newUser.setAuthRoleId(item.getAuthRoleId());
            newUser.setUserName(item.getUserName());
            newUser.setPassword(baseConstant.getDefaultPassword());
            newUser.setLockStatus(AuthUserLockStatus.UN_LOCKED.getCode());
            newUser.setRealName(item.getRealName());
            newUser.setPhoneNumber(item.getPhoneNumber());
            newUser.setDepartmentId(item.getDepartmentId());
            authUserService.addNewAuthUser(newUser);
        });
    }



    /**
     * 当前用户登出
     *
     * @date 2019-08-21
     * @param authUserId 当前用户的ID
     */
    @Override
    @Async("threadPoolTaskExecutor")
    public void asyncHandleLogOut(String authUserId){
        log.info(">>>>>>>>>>>异步处理删除登录信息参数authUserId:{}<<<<<<<<<<<",authUserId);
        log.info(">>>>>>>>>>>异步处理删除登录信息线程ID和名称:{},{}<<<<<<<<<<<",
                Thread.currentThread().getId(),
                Thread.currentThread().getName());

        String currentActiveUser = redisRepository.get(AuthConstants.AUTHENTICATED_ACTIVE_USER+authUserId);
        if(CheckParam.isNull(currentActiveUser)) {
            return;
        }
        ActiveUser activeUser = JSON.parseObject(currentActiveUser, ActiveUser.class);
        String userName = activeUser.getUserName();
        String token = activeUser.getToken();
        //删除TOKEN
        redisRepository.delete(AuthConstants.AUTHENTICATED_TOKEN + token);
        //缓存当前的用户信息 缓存的是JSON序列化之后的authUserResult
        redisRepository.delete(AuthConstants.AUTHENTICATED_USER_META_TOKEN + token);
        //在线人数 递减少
        redisRepository.decr(AuthConstants.AUTHENTICATED_ACTIVE_USERS_COUNT);
        //删除用户在线缓存
        redisRepository.delete(AuthConstants.AUTHENTICATED_ACTIVE_USER+authUserId);
        //删除用户配置
        redisRepository.delete(AuthConstants.AUTHENTICATED_USER_CONFIG+authUserId);
        //删除当前用户的权限
        redisRepository.delete(AuthConstants.AUTHENTICATED_USER_PERMISSION+userName);
        //删除当前用户的角色
        redisRepository.delete(AuthConstants.AUTHENTICATED_USER_ROLE+userName);
        //需要删除当前在线用户的集合
        String activeUserList = redisRepository.get(AuthConstants.AUTHENTICATED_ACTIVE_USERS);
        List<ActiveUser> activeList = JSON.parseArray(activeUserList, ActiveUser.class);
        activeList = activeList.stream().filter(item -> !StrUtil.equalsIgnoreCase(item.getId(),authUserId)).collect(
                Collectors.toList());
        //此处不设置过期时间
        redisRepository.set(AuthConstants.AUTHENTICATED_ACTIVE_USERS,JSON.toJSONString(activeList));

    }



    /**
      * 异步处理登录
      *
      * @date 2021/9/30
      * @param authMeta 异步认证所需的基元信息
      */
    @Override
    @Async("threadPoolTaskExecutor")
    public void handleLoginAsync(AsyncAuthMeta authMeta){

        log.info(">>>>>>>>>>>异步处理登录信息参数:{}<<<<<<<<<<<",JSON.toJSONString(authMeta));
        log.info(">>>>>>>>>>>异步处理登录信息线程ID和名称:{},{}<<<<<<<<<<<",
                Thread.currentThread().getId(),
                Thread.currentThread().getName());

        AuthLoginReq loginReq = authMeta.getLoginReq();
        AsyncAuthUserMeta authUserMeta = authMeta.getAuthUserResult();

        String authUserId = authUserMeta.getAuthUserId();
        String userName = loginReq.getUserName();

        // 保存登录记录
        SystemLoginLog loginLog = new SystemLoginLog();
        loginLog.setUserName(userName);

        String ipAddress = authMeta.getIpAddress();
        loginLog.setIp(ipAddress);
        loginLog.setLocation(ipAddress);

        //保存token等信息
        saveTokenAndCacheToRedis(authMeta);
        //更新最后登录时间
        authUserRepository.updateAuthUserLastLoginTime(authUserId, LocalDateTime.now());
        //插入登录记录
        systemLoginLogRepository.insert(loginLog);
    }


    /**
     * 保存token信息到Redis
     *
     * @date 2019-08-21
     * @param authMeta 异步认证所需的基元信息
     * @return String
     */
    private String saveTokenAndCacheToRedis(AsyncAuthMeta authMeta) {

        AsyncJwtToken jwtToken = authMeta.getJwtToken();
        AsyncAuthUserMeta asyncAuthUserMeta = authMeta.getAuthUserResult();
        String ipAddress = authMeta.getIpAddress();

        String authUserId = asyncAuthUserMeta.getAuthUserId();

        // 构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUserName(asyncAuthUserMeta.getUserName());
        activeUser.setRealName(asyncAuthUserMeta.getRealName());
        activeUser.setIp(ipAddress);
        activeUser.setToken(jwtToken.getToken());
        activeUser.setLoginAddress(ipAddress);
        activeUser.setId(authUserId);
        activeUser.setAuthUserId(authUserId);
        activeUser.setLoginTime(DateTimeUtil.localDateTime(DateTimeUtil.DEFAULT_DATETIME_PATTERN, LocalDateTime.now()));

        //对象映射
        AuthUserMeta authUserMeta = mapperFacade.map(asyncAuthUserMeta, AuthUserMeta.class);
        //setAuthData(authUserMeta);

        // redis 中存储这个加密 token，key = 前缀 + 加密 token + .ip
        redisRepository.set(AuthConstants.AUTHENTICATED_TOKEN + jwtToken.getToken(), jwtToken.getToken(), baseConstant.getAuthExpiredTime(),
                TimeUnit.MINUTES);
        //缓存当前的用户信息 缓存的是JSON序列化之后的authUserResult
        redisRepository.set(AuthConstants.AUTHENTICATED_USER_META_TOKEN + jwtToken.getToken(), JSON.toJSONString(authUserMeta),baseConstant.getAuthExpiredTime(),
                TimeUnit.MINUTES);
        //在线人数 递增
        redisRepository.incr(AuthConstants.AUTHENTICATED_ACTIVE_USERS_COUNT);

        //在线用户集合存储
        List<ActiveUser> activeList = Lists.newArrayList();
        String activeUserList = redisRepository.get(AuthConstants.AUTHENTICATED_ACTIVE_USERS);
        if(!CheckParam.isNull(activeUserList)){
            activeList = JSON.parseArray(activeUserList, ActiveUser.class);
        }
        activeList.add(activeUser);
        //此处不设置过期时间
        redisRepository.set(AuthConstants.AUTHENTICATED_ACTIVE_USERS,JSON.toJSONString(activeList));
        //存储当前在线用户
        redisRepository.set(AuthConstants.AUTHENTICATED_ACTIVE_USER+authUserId,JSON.toJSONString(activeUser),baseConstant.getAuthExpiredTime(),TimeUnit.MINUTES);
        return activeUser.getId();
    }

    /**
     * 处理用户的数据权限隔离
     *
     * @date 2021/5/10
     * @param authUserMeta 用户基元信息
     */
    public void setAuthData(AuthUserMeta authUserMeta){
        String authUserId = authUserMeta.getAuthUserId();
        Example authUserSketchExample = Example.builder(AuthUserSketch.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("authUserId", authUserId))
                .build();
        List<AuthUserSketch> authUserSketchList = authUserSketchRepository.selectByExample(authUserSketchExample);
        if(CollectionUtils.isEmpty(authUserSketchList)) {
            return;
        }
        authUserSketchList.stream().forEach(item -> {
            //处理地区权限
            if(StrUtil.equalsIgnoreCase(item.getSketchOtherType(), AuthUserOtherTypeEnum.REGION.toString())){
                authUserMeta.setRegionId(item.getSketchOtherId());
            }
        });
    }
}
