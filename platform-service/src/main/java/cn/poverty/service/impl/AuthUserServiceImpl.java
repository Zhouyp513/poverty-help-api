package cn.poverty.service.impl;


import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.common.constants.AuthConstants;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.constants.PlatformConstant;
import cn.poverty.common.entity.RouterMeta;
import cn.poverty.common.entity.VueRouter;
import cn.poverty.common.enums.ErrorCode;
import cn.poverty.common.enums.auth.AuthUserLockStatus;
import cn.poverty.common.enums.auth.data.DataRoleCodeEnum;
import cn.poverty.common.exception.BusinessException;
import cn.poverty.common.export.ExcelHandler;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.BaseQueryRepository;
import cn.poverty.common.utils.CriteriaBuilder;
import cn.poverty.common.utils.alibaba.AliMessageSendComponent;
import cn.poverty.common.utils.auth.AuthEncrypt;
import cn.poverty.common.utils.auth.EncryptUtils;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.BaseUtil;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.common.DateTimeUtil;
import cn.poverty.common.utils.common.TreeUtil;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import cn.poverty.interaction.helper.PageBuilder;
import cn.poverty.interaction.internal.auth.ActiveUser;
import cn.poverty.interaction.internal.auth.AsyncAuthMeta;
import cn.poverty.interaction.internal.auth.AsyncAuthUserMeta;
import cn.poverty.interaction.internal.auth.AsyncJwtToken;
import cn.poverty.interaction.internal.auth.AuthUserMeta;
import cn.poverty.interaction.req.auth.AuthLoginReq;
import cn.poverty.interaction.req.auth.AuthUserAddReq;
import cn.poverty.interaction.req.auth.AuthUserPageReq;
import cn.poverty.interaction.req.auth.AuthUserUpdateReq;
import cn.poverty.interaction.req.auth.UserSketchReq;
import cn.poverty.interaction.req.user.AuthUserConfigUpdateReq;
import cn.poverty.interaction.req.user.AuthUserImportReq;
import cn.poverty.interaction.req.user.UserProfileUpdateReq;
import cn.poverty.interaction.resp.auth.AuthUserConfigResp;
import cn.poverty.interaction.resp.auth.AuthorizedUserResp;
import cn.poverty.interaction.resp.auth.AuthorizedUserWrapResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.interaction.resp.role.AuthRoleResp;
import cn.poverty.interaction.resp.statistics.StatisticsVisitCountResp;
import cn.poverty.interaction.resp.statistics.VisitCountResp;
import cn.poverty.interaction.resp.user.AuthUserExportResp;
import cn.poverty.interaction.resp.user.AuthUserPageQueryResp;
import cn.poverty.interaction.resp.user.AuthUserSketchResp;
import cn.poverty.repository.entity.AuthDepartment;
import cn.poverty.repository.entity.AuthRole;
import cn.poverty.repository.entity.AuthUser;
import cn.poverty.repository.entity.AuthUserConfig;
import cn.poverty.repository.entity.AuthUserRole;
import cn.poverty.repository.entity.AuthUserSketch;
import cn.poverty.repository.entity.SystemLoginLog;
import cn.poverty.repository.repository.AuthDepartmentRepository;
import cn.poverty.repository.repository.AuthMenuRepository;
import cn.poverty.repository.repository.AuthPermissionRepository;
import cn.poverty.repository.repository.AuthRoleRepository;
import cn.poverty.repository.repository.AuthUserConfigRepository;
import cn.poverty.repository.repository.AuthUserRepository;
import cn.poverty.repository.repository.AuthUserRoleRepository;
import cn.poverty.repository.repository.AuthUserSketchRepository;
import cn.poverty.repository.repository.SystemLoginLogRepository;
import cn.poverty.repository.result.AuthMenuQueryResult;
import cn.poverty.repository.result.AuthPermissionQueryResult;
import cn.poverty.repository.result.AuthRoleResult;
import cn.poverty.repository.result.AuthUserResult;
import cn.poverty.service.AsyncHandlerService;
import cn.poverty.service.AuthCacheService;
import cn.poverty.service.AuthUserService;
import cn.poverty.service.config.shiro.JwtToken;
import cn.poverty.service.config.shiro.JwtUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 * @projectName poverty-help-api
 * @Description:
 * @date 2019-08-13
 */
@Service(value = "authUserService")
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {

    @Resource
    private AuthUserRepository authUserRepository;

    @Resource
    private AuthPermissionRepository authPermissionRepository;

    @Resource
    private AuthRoleRepository authRoleRepository;

    @Resource
    private AuthUserRoleRepository authUserRoleRepository;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthCacheService authCacheService;

    @Resource
    private SystemLoginLogRepository systemLoginLogRepository;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private AuthUserConfigRepository authUserConfigRepository;

    @Resource
    private AuthMenuRepository authMenuRepository;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private AliMessageSendComponent aliMessageSendComponent;

    @Resource
    private PlatformConstant platformConstant;

    @Resource
    private AuthDepartmentRepository authDepartmentRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private AuthUserSketchRepository authUserSketchRepository;

    @Resource
    private HttpServletRequest request;

    @Resource
    private AsyncHandlerService asyncHandlerService;

    @Resource
    private IExcelDictHandler excelDictHandler;


    /**
     * 返回当前用户的业务主键ID
     *
     * @date 2021/5/22
     * @return java.lang.String
     */
    @Override
    public String currentAuthUserId(){
        AuthUserMeta authUserMeta = currentUserMeta(true);

        if(CheckParam.isNull(authUserMeta)){
            throw new BusinessException(ErrorCode.NO_AUTH_META_ERROR.getCode(),
                    ErrorCode.NO_AUTH_META_ERROR.getMessage());
        }

        return authUserMeta.getAuthUserId();
    }

    /**
      * 返回当前用户的角色代码
      *
      * @date 2021/5/22
      * @return java.lang.String
      */
    @Override
    public String currentUserRoleCode(){
        AuthUserMeta authUserMeta = currentUserMeta(true);

        if(CheckParam.isNull(authUserMeta)){
            throw new BusinessException(ErrorCode.NO_AUTH_META_ERROR.getCode(),
                    ErrorCode.NO_AUTH_META_ERROR.getMessage());
        }

        return authUserMeta.getRoleCode();
    }


    /**
      * 当前用户的缓存信息
      * @author
      * @date 2019-08-29
      * @param needThrow 为空是否需要抛出
      * @return AuthUserCache
      */
    @Override
    public AuthUserMeta currentUserMeta(Boolean needThrow){
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String token;
        //此处拿不到认证信息的原因有可能是因为接口是匿名访问的
        if(!CheckParam.isNull(principal)){
            token = AuthEncrypt.encryptToken(String.valueOf(principal));
        }else{
            token = request.getHeader(AuthConstants.TOKEN);
        }
        String authUserMeta = redisRepository.get(AuthConstants.AUTHENTICATED_USER_META_TOKEN + token);
        if(CheckParam.isNull(authUserMeta)) {
            if(needThrow){
                throw new BusinessException(ErrorCode.NO_AUTH_META_ERROR.getCode(),
                        ErrorCode.NO_AUTH_META_ERROR.getMessage());
            }else{
                return null;
            }
        }
        //进行反解析
        return JSON.parseObject(authUserMeta, AuthUserMeta.class);
    }


    /**
      *  统计系统访问总量
      *
      * @date 2019-08-22
      * @param  userName 用户名
      * @return StatisticsVisitCountResp
      */
    @Override
    public StatisticsVisitCountResp statisticsVisitCount(String userName){
        StatisticsVisitCountResp resp = new StatisticsVisitCountResp();
        resp.setTotalVisitCount(systemLoginLogRepository.queryTotalVisitCount(baseConstant.getUnDeleteStatus()));
        resp.setTodayVisitCount(systemLoginLogRepository.queryTodayVisitCount(baseConstant.getUnDeleteStatus()));
        resp.setTodayIp(systemLoginLogRepository.queryTodayIp(baseConstant.getUnDeleteStatus()));
        //计算出七天前的时间
        String sevenDaysAgo = DateTimeUtil
                .localDateTime(DateTimeUtil.DEFAULT_DATETIME_PATTERN, LocalDateTime.now().minusDays(7L));
        Example systemLoginLogExample = Example.builder(SystemLoginLog.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andGreaterThanOrEqualTo("loginTime", sevenDaysAgo))
                .build();
        //查询出整个系统七天的访问量
        List<SystemLoginLog> sevenDaysAgoLoginLogList = systemLoginLogRepository.selectByExample(systemLoginLogExample);
        if(!sevenDaysAgoLoginLogList.isEmpty()){
            //整个系统七天的访问量
            List<VisitCountResp> allLastSevenVisitCount = new ArrayList<>();
            //此处需要按照时间进行分组求和
            Map<Date, Long> lastSevenVisitCountGroupedHashMap = sevenDaysAgoLoginLogList.stream().collect(Collectors.groupingBy(SystemLoginLog::getLoginTime, Collectors.counting()));
            lastSevenVisitCountGroupedHashMap.forEach((k,v) -> {
                VisitCountResp visitCountResp = new VisitCountResp();
                //key的Date类型变成LocalDateTime
                LocalDateTime keyLocalDate = k.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                visitCountResp.setCount(v);
                visitCountResp.setDays(DateTimeUtil.localDateTime("MM-dd", keyLocalDate));
                allLastSevenVisitCount.add(visitCountResp);
            });
            resp.setLastSevenVisitCount(allLastSevenVisitCount);
            //需要统计出访问记录等于当前用户的用户登陆记录
            List<SystemLoginLog> currentUserVisitLogList = sevenDaysAgoLoginLogList.stream().filter(n1 -> n1.getUserName().equalsIgnoreCase(userName)).collect(Collectors.toList());
            //当前用户最近七天的访问记录
            List<VisitCountResp> currentUserAllLastSevenVisitCount = new ArrayList<>();
            if(!CheckParam.isNull(currentUserVisitLogList) && !currentUserVisitLogList.isEmpty()){
                //此处需要按照时间进行分组求和
                Map<Date, Long> currentUserLastSevenVisitCountGroupedHashMap = currentUserVisitLogList.stream().collect(Collectors.groupingBy(SystemLoginLog::getLoginTime, Collectors.counting()));
                currentUserLastSevenVisitCountGroupedHashMap.forEach((k,v) -> {
                    VisitCountResp currentUserVisitCountResp = new VisitCountResp();
                    //key的Date类型变成LocalDateTime
                    LocalDateTime keyLocalDate = k.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    currentUserVisitCountResp.setCount(v);
                    currentUserVisitCountResp.setDays(DateTimeUtil.localDateTime("MM-dd", keyLocalDate));
                    currentUserAllLastSevenVisitCount.add(currentUserVisitCountResp);
                });
                resp.setLastSevenUserVisitCount(currentUserAllLastSevenVisitCount);
            }
        }
        return resp;
    }


    /**
      * 根据用户名查询用户能访问的菜单
      *
      * @date 2019-08-22
      * @param userName 用户名称
      * @return java.util.List
      */
    @Override
    public List<VueRouter<AuthMenuQueryResult>> queryVueRouterByUserName(String userName){
        List<AuthMenuQueryResult> authMenuQueryResultList = authMenuRepository.queryAuthMenuByUserName(baseConstant.getUnDeleteStatus(), userName);
        if(CollectionUtils.isEmpty(authMenuQueryResultList)) {
            return new ArrayList<>();
        }
        List<VueRouter<AuthMenuQueryResult>> routes = new ArrayList<>();
        authMenuQueryResultList.forEach(menu -> {
            VueRouter<AuthMenuQueryResult> route = new VueRouter<>();
            route.setId(menu.getAuthMenuId());
            route.setParentId(menu.getParentId());
            route.setIcon(menu.getMenuIcon());
            route.setPath(menu.getPath());
            route.setComponent(menu.getComponent());
            route.setName(menu.getMenuName());
            route.setMeta(new RouterMeta(true, null));
            routes.add(route);
        });
        return TreeUtil.buildVueRouter(routes);
    }


    /**
     * 导入数据 (目前只导入贫困用户)
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2021/4/22
     * @param file 文件
     */
    @Override
    public void importData(MultipartFile file){
        List<AuthUserImportReq> resultList =
                ExcelHandler.importExcel(file, BigInteger.ONE.intValue(),  BigInteger.ONE.intValue(),
                        AuthUserImportReq.class,excelDictHandler);
        if(CollectionUtil.isEmpty(resultList)) {
            throw new BusinessException(ErrorCode.NO_IMPORT_DATA_ERROR.getCode(),
                    ErrorCode.NO_IMPORT_DATA_ERROR.getMessage());
        }
        asyncHandlerService.importUser(resultList);
    }

    /**
     * 系统用户导出
     * @author
     * @date 2019-08-21
     * @param req 用户信息导出Req
     * @return cn.poverty.interaction.resp.page.Pagination
     */
    @Override
    public List<AuthUserExportResp> exportData(AuthUserPageReq req){
        log.info(">>>>>>>>>>>>>用户信息导出<<<<<<<<<<<<<<<<<<");
        Boolean needData = req.getNeedData();
        if(!needData){
            return Lists.newArrayList();
        }
        String roleCode = req.getRoleCode();
        //首先查询具备贫困户角色的用户ID
        List<String> authUserIdList = authUserRoleRepository.queryUserIdByRoleCode(roleCode);
        if(CollectionUtils.isEmpty(authUserIdList)){
            return Lists.newArrayList();
        }
        //构建查询条件
        Example authUserExample = new Example(AuthUser.class);
        Example.Criteria authUserCriteria = authUserExample.createCriteria();
        CriteriaBuilder.rigidCriteria(authUserCriteria);
        if(!CheckParam.isNull(req.getDepartmentId())){
            authUserCriteria.andEqualTo("departmentId", req.getDepartmentId());
        }
        if(!CheckParam.isNull(req.getUserName())){
            authUserCriteria.andLike("userName", "%"+req.getUserName()+"%");
        }
        if(!CheckParam.isNull((req.getBeginTime()))){
            authUserCriteria.andGreaterThanOrEqualTo("createTime",req.getBeginTime());
        }
        if(!CheckParam.isNull((req.getEndTime()))){
            authUserCriteria.andLessThanOrEqualTo("createTime",req.getEndTime());
        }
        authUserCriteria.andIn("authUserId",authUserIdList);
        authUserExample.orderBy("createTime").desc();
        //开始分页
        List<AuthUser> entityList = authUserRepository.selectByExample(authUserExample);
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        //查询用户的角色信息
        List<AuthRoleResult> authRoleResultList = authRoleRepository.queryUserRoleByUserIdList(baseConstant.getUnDeleteStatus(), authUserIdList);
        //形成对应map
        HashMap<String, AuthRoleResult> authUserRoleHashMap = authRoleResultList.stream().collect(Collectors.toMap(AuthRoleResult::getAuthUserId, a -> a, (k1, k2) -> k1, HashMap::new));
        List<AuthUserExportResp> respList = Lists.newArrayList();
        entityList.stream().forEach(item -> {
            AuthUserExportResp resp = mapperFacade.map(item, AuthUserExportResp.class);
            AuthRoleResult authRoleResult = authUserRoleHashMap.get(item.getAuthUserId());
            if(!CheckParam.isNull(authRoleResult)){
                resp.setAuthRoleId(authRoleResult.getAuthRoleId());
            }
            respList.add(resp);
        });
        return respList;
    }

    /**
      * 用户分页查询
      * @author
      * @date 2019-08-21
      * @param authUserPageReq 用户信息分页查询Req
      * @return cn.poverty.interaction.resp.page.Pagination
      */
    @Override
    public Pagination<AuthUserPageQueryResp> queryUserByPage(AuthUserPageReq authUserPageReq){
        log.info(">>>>>>>>>>>>>分页查询用户信息<<<<<<<<<<<<<<<<<<");
        //构建查询条件
        Example pageExample = new Example(AuthUser.class);
        Example.Criteria pageCriteria = pageExample.createCriteria();
        CriteriaBuilder.rigidCriteria(pageCriteria);
        if(!CheckParam.isNull(authUserPageReq.getDepartmentId())){
            pageCriteria.andEqualTo("departmentId", authUserPageReq.getDepartmentId());
        }
        if(!CheckParam.isNull(authUserPageReq.getUserName())){
            pageCriteria.andLike("userName", "%"+authUserPageReq.getUserName()+"%");
        }
        if(!CheckParam.isNull((authUserPageReq.getBeginTime()))){
            pageCriteria.andGreaterThanOrEqualTo("createTime",authUserPageReq.getBeginTime());
        }
        if(!CheckParam.isNull((authUserPageReq.getEndTime()))){
            pageCriteria.andLessThanOrEqualTo("createTime",authUserPageReq.getEndTime());
        }
        pageExample.orderBy("createTime").desc();
        //开始分页
        Page<Object> page = PageHelper.startPage(authUserPageReq.getCurrentPage(), authUserPageReq.getItemsPerPage());
        List<AuthUser> authUserList = authUserRepository.selectByExample(pageExample);
        if(CollectionUtils.isEmpty(authUserList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<String> authUserIdList = authUserList.stream().filter(u1 -> !CheckParam.isNull(u1.getAuthUserId())).map(u1 -> u1.getAuthUserId()).collect(Collectors.toList());
        List<String> departmentIdList = authUserList.stream().filter(u1 -> !CheckParam.isNull(u1.getDepartmentId())).map(u1 -> u1.getDepartmentId()).collect(Collectors.toList());
        Example authUserSketchExample = Example.builder(AuthUserSketch.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andIn("authUserId", authUserIdList))
                .build();
        List<AuthUserSketch> authUserSketchList = authUserSketchRepository.selectByExample(authUserSketchExample);
        //形成对应简略信息map
        HashMap<String, AuthUserSketch> authUserSketchHashMap = authUserSketchList.stream().collect(Collectors.toMap(AuthUserSketch::getAuthUserId, a -> a, (k1, k2) -> k1, HashMap::new));
        //查询用户的角色信息
        List<AuthRoleResult> authRoleResultList = authRoleRepository.queryUserRoleByUserIdList(baseConstant.getUnDeleteStatus(), authUserIdList);
        //形成对应map
        HashMap<String, AuthRoleResult> authUserRoleHashMap = authRoleResultList.stream().collect(Collectors.toMap(AuthRoleResult::getAuthUserId, a -> a, (k1, k2) -> k1, HashMap::new));
        List<AuthDepartment> authDepartmentList = Lists.newArrayList();
        if(!CollectionUtil.isEmpty(departmentIdList)){
            Example authDepartmentExample = Example.builder(AuthDepartment.class).where(Sqls.custom()
                    .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                    .andIn("departmentId", departmentIdList))
                    .build();
            List<AuthDepartment> departmentList = authDepartmentRepository.selectByExample(authDepartmentExample);
            authDepartmentList.addAll(departmentList);
        }
        HashMap<String, AuthDepartment> authDepartmentHashMap = authDepartmentList.stream().collect(Collectors.toMap(AuthDepartment::getDepartmentId, a -> a, (k1, k2) -> k1, HashMap::new));
        List<AuthUserPageQueryResp> authUserRespList = new ArrayList<>();
        authUserList.stream().forEach(item -> {
            AuthUserPageQueryResp resp = new AuthUserPageQueryResp();
            resp.setId(item.getId());
            resp.setAuthCacheKey(item.getAuthUserId());
            resp.setAuthUserId(item.getAuthUserId());
            resp.setDepartmentId(item.getDepartmentId());
            resp.setAvatar(item.getAvatar());
            resp.setCreateTime(item.getCreateTime());
            resp.setDescription(item.getDescription());
            resp.setLastLoginTime(item.getLastLoginTime());
            resp.setLockStatus(item.getLockStatus());
            resp.setPhoneNumber(item.getPhoneNumber());
            resp.setPassword(item.getAuthPassword());
            resp.setUpdateTime(item.getUpdateTime());
            resp.setUserName(item.getUserName());
            resp.setRealName(item.getRealName());
            resp.setPassword("it's a secret");
            AuthUserSketch authUserSketch = authUserSketchHashMap.get(item.getAuthUserId());
            if(!CheckParam.isNull(authUserSketch)){
                resp.setSketchOtherType(authUserSketch.getSketchOtherType());
                resp.setSketchOtherId(authUserSketch.getSketchOtherId());
            }
            AuthRoleResult authRoleResult = authUserRoleHashMap.get(item.getAuthUserId());
            if(!CheckParam.isNull(authRoleResult)){
                resp.setAuthRoleId(authRoleResult.getAuthRoleId());
                resp.setRoleName(authRoleResult.getRoleName());
            }
            AuthDepartment authDepartment = authDepartmentHashMap.get(item.getDepartmentId());
            if(!CheckParam.isNull(authDepartment)){
                resp.setDepartmentName(authDepartment.getDepartmentName());
                resp.setDepartmentId(authDepartment.getDepartmentId());
            }
            authUserRespList.add(resp);
        });
        return PageBuilder.buildPageResult(page,authUserRespList);
    }

    /**
      * 根据用户名称判断用户是否存在
      *
      * @date 2019-08-21
      * @param userName 用户名称
      * @return Boolean
      */
    @Override
    public Boolean judgeUserExistByUserName(String userName){
        Example authUserExample = Example.builder(AuthUser.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("userName", userName))
                .build();
        AuthUser authUser = authUserRepository.selectOneByExample(authUserExample);
        return CheckParam.isNull(authUser);
    }


    /**
      * 根据用户名称查询用户信息
      *
      * @date 2019-08-21
      * @param userName 用户名称
      * @return AuthorizedUserResp
      */
    @Override
    public AuthorizedUserResp queryAuthorizedUserByUserName(String userName){
        AuthUserResult authUserResult = queryAuthUserByName(userName);
        if(CheckParam.isNull(authUserResult)) {
            return new AuthorizedUserResp();
        }
        AuthorizedUserResp resp = new AuthorizedUserResp();
        setAuthorizedUserRespData(resp,authUserResult);
        return resp;
    }



    /**
      * 用户登陆逻辑
      *
      * @date 2019-08-21
      * @param loginReq 登录请求参数
      * @return cn.poverty.interaction.resp.auth.AuthorizedUserWrapResp
      */
    @Override
     public AuthorizedUserWrapResp authUserLogin(AuthLoginReq loginReq){
        Long beginTime = System.currentTimeMillis();
        log.info(">>>>>>>>>>>>>管理系统用户登陆<<<<<<<<<<<<<<<<<<");
        String password = loginReq.getPassword();
        String userName = loginReq.getUserName();
        //查询当前登陆用户
        AuthUserResult authUserResult = queryAuthUserByName(userName);
        if (CheckParam.isNull(authUserResult)) {
            throw new BusinessException(ErrorCode.AUTH__ERROR.getCode(),"用户不存在");
        }
        boolean passwordVerify = EncryptUtils.verifyPassword(authUserResult.getAuthPassword(), password, authUserResult.getAuthSalt());
        if (!passwordVerify) {
            throw new BusinessException(ErrorCode.AUTH__ERROR.getCode(),"密码错误");
        }
        //用户锁定状态 1:锁定,2:有效
        if (PlatformConstant.STATUS_LOCK.compareTo(authUserResult.getLockStatus()) == 0) {
            throw new BusinessException(ErrorCode.AUTH__ERROR.getCode(),"账号已被锁定,请联系管理员");
        }
        String authUserId = authUserResult.getAuthUserId();
        //控制单点登录
        String currentActiveUser = redisRepository.get(AuthConstants.AUTHENTICATED_ACTIVE_USER+authUserId);
        if(!CheckParam.isNull(currentActiveUser)){
            //throw new BusinessException(ErrorCode.BUSINESS_REFUSE.getCode(),"当前用户已经登录,请先退出登录");
            authUserLogOut(authUserId);
        }
        //登陆的token处理
        String token = AuthEncrypt.encryptToken(JwtUtil.sign(userName, password));
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(baseConstant.getAuthExpiredTime());
        String expireTimeStr = DateTimeUtil.convertTimeToString(DateTimeUtil.DEFAULT_DATETIME_PATTERN, expireTime);
        JwtToken jwtToken = new JwtToken(token, expireTimeStr);

        String ipAddress = BaseUtil.getIpAddress(request);
        AsyncAuthMeta authMeta = new AsyncAuthMeta();
        authMeta.setLoginReq(loginReq);
        authMeta.setAuthUserResult(mapperFacade.map(authUserResult, AsyncAuthUserMeta.class));
        authMeta.setJwtToken(mapperFacade.map(jwtToken, AsyncJwtToken.class));
        authMeta.setIpAddress(ipAddress);
        //异步处理
        asyncHandlerService.handleLoginAsync(authMeta);

        //生成返回数据
        AuthorizedUserWrapResp resp = generateUserMeta(jwtToken, authUserResult);
        Long endTime = System.currentTimeMillis();
        log.info(">>>>>>>>>>>>>>登录成功管理系统用户登录返回:{}<<<<<<<<<<<<<",JSON.toJSONString(resp));
        log.info(">>>>>>>>>>>>>>登录成功管理系统用户登录耗时:{}<<<<<<<<<<<<<",(endTime-beginTime));
        return resp;
     }


    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. Vue Router
     * 3. 用户角色
     * 4. 用户权限
     * 5. 前端系统个性化配置信息
     * @param token token
     * @param authUserResult  用户信息
     * @return AuthorizedUserWrapResp
     */
    private AuthorizedUserWrapResp generateUserMeta(JwtToken token, AuthUserResult authUserResult) {

            AuthorizedUserWrapResp resp = new AuthorizedUserWrapResp();

            String userName = authUserResult.getUserName();
            resp.setToken(token.getToken());
            resp.setExpireTime(DateTimeUtil.convertStringToLongTime(token.getExipreAt()));

            List<AuthRoleResult> authRoleResults = authRoleRepository.queryUserRoleDataByUserName(baseConstant.getUnDeleteStatus(), userName);
            //设置角色名称信息
            resp.setRoles(authRoleResults.stream().map(u1 -> u1.getRoleName()).collect(Collectors.toList()));

            List<AuthPermissionQueryResult> permissionList = BaseQueryRepository.queryByFunctional(
                    () -> authCacheService.getPermissions(userName),
                    () -> authUserRepository.queryAuthUserPermissionByUserName(baseConstant.getUnDeleteStatus(), userName));

            resp.setPermissions(permissionList.stream().filter(n1 -> !CheckParam.isNull(n1.getPerms())).map(AuthPermissionQueryResult::getPerms).collect(Collectors.toList()));

            Example authUserConfigExample = Example.builder(AuthUserConfig.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("authUserId", authUserResult.getAuthUserId()))
                .build();

            //查询用户的配置并且设置到换回值里面
            AuthUserConfig authUserConfig = authUserConfigRepository.selectOneByExample(authUserConfigExample);

            if(!CheckParam.isNull(authUserConfig)){
                AuthUserConfigResp config = new AuthUserConfigResp();

                config.setColor(authUserConfig.getColor());
                config.setFixHeader(authUserConfig.getFixHeader());
                config.setFixSiderBar(authUserConfig.getFixSiderBar());
                config.setLayout(authUserConfig.getLayout());
                config.setMultiPage(authUserConfig.getMultiPage());
                config.setSystemUserConfigId(authUserConfig.getSystemUserConfigId());
                config.setTheme(authUserConfig.getTheme());
                config.setAuthUserId(authUserConfig.getAuthUserId());

                resp.setConfig(config);
            }
            AuthorizedUserResp user = new AuthorizedUserResp();
            setAuthorizedUserRespData(user,authUserResult);
            resp.setUser(user);

            return resp;
    }

     /**
      * 设置用户详细信息
      *
      * @date 2019-08-21
      * @param resp 用户信息
      * @param authUserResult 用户详细信息
      */
     private void setAuthorizedUserRespData(AuthorizedUserResp resp,AuthUserResult authUserResult){
            resp.setAuthCacheKey(authUserResult.getAuthUserId());
            resp.setAuthUserId(authUserResult.getAuthUserId());
            resp.setAvatar(authUserResult.getAvatar());
            resp.setCreateTime(authUserResult.getCreateTime());
            resp.setDepartmentId(authUserResult.getDepartmentId());
            resp.setDepartmentName(authUserResult.getDepartmentName());
            resp.setDescription(authUserResult.getDescription());
            resp.setId(authUserResult.getAuthUserId());
            resp.setLastLoginTime(authUserResult.getLastLoginTime());
            resp.setLockStatus(authUserResult.getLockStatus());
            resp.setPhoneNumber(authUserResult.getPhoneNumber());
            resp.setPassword(authUserResult.getAuthPassword());
            resp.setAuthRoleId(authUserResult.getAuthRoleId());
            resp.setRoleName(authUserResult.getRoleName());
            resp.setRoleCode(authUserResult.getRoleCode());
            resp.setGender(authUserResult.getGender());
            resp.setUpdateTime(authUserResult.getUpdateTime());
            resp.setUserName(authUserResult.getUserName());
            resp.setRealName(authUserResult.getRealName());
            resp.setPassword("it's a secret");
     }

    /**
     * 当前用户登出
     *
     * @date 2019-08-21
     * @param authUserId 当前用户的ID
     */
    @Override
    public void authUserLogOut(String authUserId){
        asyncHandlerService.asyncHandleLogOut(authUserId);
        //执行登出方法
        SecurityUtils.getSubject().logout();
    }

    /**
     * 查询当前所有在线的用户
     *
     * @date 2019-08-23
     * @return List
     */
    @Override
    public List<ActiveUser> queryAllActiveUser(){
        String activeUserList = redisRepository.get(AuthConstants.AUTHENTICATED_ACTIVE_USERS);
        if(!CheckParam.isNull(activeUserList)){
            List<ActiveUser> activeList = JSON.parseArray(activeUserList, ActiveUser.class);
            activeList.forEach(item -> {
                try{
                    item.setToken(null);
                }catch (Exception e){
                    log.error(">>>>>>>>>>>>>>>>>>>>>查询在线用户处理数据出现异常:{},{}<<<<<<<<<<<<<<<<<<<",e.getMessage(),e);
                }
            });
            return activeList;
        }
        return new ArrayList<>();
    }

    /**
      * 根据当前用户名查询当前活跃的用户
      *
      * @date 2019-08-23
      * @param userName 当前用户名
      * @return List
      */
    @Override
    public List<ActiveUser> queryActiveUserByUserName(String userName){

        HashSet<String> activeUserHashSet = redisRepository.keys(AuthConstants.AUTHENTICATED_ACTIVE_USERS);

        if(!CheckParam.isNull(activeUserHashSet) && !activeUserHashSet.isEmpty()){

            List<ActiveUser> activeUsers = new ArrayList<>();

            activeUserHashSet.forEach(u1 -> {
                try{
                    ActiveUser activeUser = JSON.parseObject(u1, ActiveUser.class);
                    activeUser.setToken(null);
                    if(!CheckParam.isNull(userName)){
                        if(activeUser.getUserName().equalsIgnoreCase(userName)){
                            activeUsers.add(activeUser);
                        }
                    }else{
                        activeUsers.add(activeUser);
                    }
                }catch (Exception e){
                    log.error(">>>>>>>>>>>>>>>>>>>>>查询在线用户处理数据出现异常:{},{}<<<<<<<<<<<<<<<<<<<",e.getMessage(),e);
                }
            });

            return activeUsers;
        }
        return new ArrayList<>();
    }



    /**
      * 根据用户名查询用户信息
      *
      * @date 2019-08-21
      * @param userName 用户名
      * @return AuthUserResult
      */
    @Override
    public AuthUserResult queryAuthUserByName(String userName){
        //使用函数式查询方式查询数据，先查询缓存如果查询不到再查询数据库
        AuthUserResult authUserResult = BaseQueryRepository.queryByFunctional(
                () -> authCacheService.queryUserCacheByKey(userName,AuthUserResult.class),
                () -> authUserRepository.queryAuthUserSketchByUserName(baseConstant.getUnDeleteStatus(),
                        userName));

        return authUserResult;
    }


    /**
      * 查询所有用户的简略信息
      *
      * @date 2021/4/6
      * @return List
      */
    @Override
    public List<AuthUserSketchResp> queryAllAuthUserSketch(){
        Example  authUserExample = Example.builder(AuthUser.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus()))
                .build();

        List<AuthUser> authUserList = authUserRepository.selectByExample(authUserExample);

        if(CollectionUtils.isEmpty(authUserList)) {
            return Lists.newArrayList();
        }

        List<AuthUserSketchResp> respList = mapperFacade.mapAsList(authUserList, AuthUserSketchResp.class);

        return respList;
    }



    /**
      * 更新用户资料
      * @author
      * @date 2021/4/18
      * @param userProfileUpdateReq 更新用户资料请求参数
      */
    @Override
    public void updateAuthUserProfile(UserProfileUpdateReq userProfileUpdateReq){
        log.info(">>>>>>>>>>>>>>>>>>>>>>用户资料修改请求参数:{}<<<<<<<<<<<<<<<<<<<<",userProfileUpdateReq.toString());
        //拿到当前用户
        AuthUserMeta authUserMeta = this.currentUserMeta(true);
        String authUserId = authUserMeta.getAuthUserId();
        Example  authUserExample = Example.builder(AuthUser.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("authUserId", authUserId))
                .build();
        AuthUser authUser = authUserRepository.selectOneByExample(authUserExample);
        if(CheckParam.isNull(authUser)){
            return;
        }
        setUpdateAuthUserProfile(userProfileUpdateReq,authUser);
        authUserRepository.updateByPrimaryKeySelective(authUser);
    }


    /**
      * 设置系统用户需要被更新的数据
      *
      * @date 2021/4/17
      * @param userProfileUpdateReq 需要更新的数据
      * @param authUser 用户数据
      */
    public void setUpdateAuthUserProfile(UserProfileUpdateReq userProfileUpdateReq,AuthUser authUser){
        if(!CheckParam.isNull(userProfileUpdateReq.getDescription())){
            authUser.setDescription(userProfileUpdateReq.getDescription());
        }
        if(!CheckParam.isNull(userProfileUpdateReq.getPhoneNumber())){
            authUser.setPhoneNumber(userProfileUpdateReq.getPhoneNumber());
        }
        if(!CheckParam.isNull(userProfileUpdateReq.getAvatar())){
            authUser.setAvatar(userProfileUpdateReq.getAvatar());
        }
    }

    /**
      * 系统定制修改

      * @date 2019/8/29
      * @param userConfig 系统定制修改参数
      */
    @Override
    public void updateUserConfig(AuthUserConfigUpdateReq userConfig) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>系统定制修改参数:{}<<<<<<<<<<<<<<<<<<<<",userConfig.toString());
        //拿到当前用户
        AuthUserMeta authUserMeta = this.currentUserMeta(true);
        //查询
        Example  authUserConfigExample = Example.builder(AuthUserConfig.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("authUserId", authUserMeta.getAuthUserId()))
                .build();
        AuthUserConfig authUserConfig = authUserConfigRepository.selectOneByExample(authUserConfigExample);
        //设值
        if (!CheckParam.isNull(authUserConfig)){
            if (StringUtils.isNotBlank(userConfig.getColor())){
                authUserConfig.setColor(userConfig.getColor());
            }
            if (StringUtils.isNotBlank(userConfig.getLayout())){
                authUserConfig.setLayout(userConfig.getLayout());
            }
            if (StringUtils.isNotBlank(userConfig.getMultiPage())){
                authUserConfig.setMultiPage(userConfig.getMultiPage());
            }
            if (StringUtils.isNotBlank(userConfig.getTheme())){
                authUserConfig.setTheme(userConfig.getTheme());
            }
            if (!CheckParam.isNull(userConfig.getFixHeader())){
                authUserConfig.setFixHeader(userConfig.getFixHeader());
            }
            if (!CheckParam.isNull(userConfig.getFixSiderBar())){
                authUserConfig.setFixSiderBar(userConfig.getFixSiderBar());
            }
            authUserConfigRepository.updateByPrimaryKeySelective(authUserConfig);
        }

    }

    /**
     * 更新密码

     * @date 2019/8/29
     * @param userName 用户名
     * @param password 密码
     */
    @Override
    public void updatePassword(String userName, String password) {
        //根据userName查询数据
        Example  authUserExample = Example.builder(AuthUser.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("userName", userName))
                .build();
        AuthUser authUser = authUserRepository.selectOneByExample(authUserExample);
        if (!CheckParam.isNull(authUser)){
            String authSalt = SnowflakeIdWorker.uniqueSequenceStr();
            //生产新密码
            String encrypt = EncryptUtils.encrypt(password,authSalt);
            authUser.setAuthPassword(encrypt);
            authUser.setAuthSalt(authSalt);
            authUserRepository.updateByPrimaryKey(authUser);
        }else {
            throw new BusinessException(ErrorCode.ERROR_UPDATE_PASSWORD.getCode(),
                    ErrorCode.ERROR_UPDATE_PASSWORD.getMessage());
        }

    }

    /**
      * 检验旧密码,如果输入的旧密码和原本设置的密码不同则不通过

      * @date 2019/8/30
      * @param  userName 用户名
      * @param  password 密码
      */
    @Override
    public void checkOldPassword(String userName, String password) {
        //拿到当前用户
        AuthUserMeta authUserMeta = this.currentUserMeta(true);
        String authUserId = authUserMeta.getAuthUserId();
        String authPassword = authUserRepository.queryPasswordByUserId(authUserId, baseConstant.getUnDeleteStatus());
        //对旧密码进行加密
        String authSalt = authUserMeta.getAuthSalt();
        String encrypt = EncryptUtils.encrypt(password, authSalt);
        if (!StrUtil.equals(encrypt,authPassword,false)){
            throw new BusinessException(ErrorCode.OLD_PASSWORD_VERIFY_PASSWORD.getCode(),
                    ErrorCode.OLD_PASSWORD_VERIFY_PASSWORD.getMessage());
        }
    }

    /**
     * 检验用户名

     * @date 2019/8/30
     * @param  userName 用户名
     */
    @Override
    public void checkUserName(String userName) {
        //查询出对应的用户
        Example  authUserExample = Example.builder(AuthUser.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("userName", userName))
                .build();
        AuthUser authUser = authUserRepository.selectOneByExample(authUserExample);
        if (null != authUser){
            throw new BusinessException(ErrorCode.AUTH_USER_EXIST_CODE_ERROR.getCode(),
                    ErrorCode.AUTH_USER_EXIST_CODE_ERROR.getMessage());
        }
    }


    /**
      * 查询所有角色
      *
      * @date 2021/4/6
      * @return List
      */
    @Override
    public List<AuthRoleResp> queryAllAuthRole(){
        Example  authRoleExample = Example.builder(AuthRole.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus()))
                .build();

        List<AuthRole> authRoleList = authRoleRepository.selectByExample(authRoleExample);

        if(CollectionUtils.isEmpty(authRoleList)) {
            return Lists.newArrayList();
        }

        List<AuthRoleResp> respList = mapperFacade.mapAsList(authRoleList, AuthRoleResp.class);

        return respList;
    }

    /**
      * 修改用户信息

      * @date 2019/8/31
      * @param updateReq 修改用户信息请求参数
      */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(AuthUserUpdateReq updateReq) {
        //查询出对应的用户
        Example  authUserExample = Example.builder(AuthUser.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("authUserId", updateReq.getAuthUserId()))
                .build();
        AuthUser authUser = authUserRepository.selectOneByExample(authUserExample);
        if(CheckParam.isNull(authUser)){
            return;
        }
        String authUserId = authUser.getAuthUserId();
        //设置更新内容
        if (StringUtils.isNotBlank(updateReq.getPhoneNumber()) &&
                !authUser.getPhoneNumber().equalsIgnoreCase(updateReq.getPhoneNumber())){
            authUser.setPhoneNumber(updateReq.getPhoneNumber());
        }
        if (!CheckParam.isNull(updateReq.getLockStatus())){
            authUser.setLockStatus(updateReq.getLockStatus());
        }
        if (!CheckParam.isNull(updateReq.getDepartmentId())){
            authUser.setDepartmentId(updateReq.getDepartmentId());
        }
        if (!CheckParam.isNull(updateReq.getRealName())){
            authUser.setRealName(updateReq.getRealName());
        }
        if (!CheckParam.isNull(updateReq.getUserName())){
            authUser.setUserName(updateReq.getUserName());
        }
        authUserRepository.updateByPrimaryKey(authUser);
        //处理用户简略信息类型
        List<UserSketchReq> sketchList = updateReq.getSketchList();
        if(!CollectionUtils.isEmpty(sketchList)){
            insertUserSketchList(authUserId,sketchList);
        }
        //特殊处理角色
        if (StringUtils.isNotBlank(updateReq.getAuthRoleId())){
            //查询原有角色
            Example  authUserRoleExample = Example.builder(AuthUserRole.class).where(Sqls.custom()
                    .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                    .andEqualTo("authUserId", updateReq.getAuthUserId()))
                    .build();
            AuthUserRole authUserRole = authUserRoleRepository.selectOneByExample(authUserRoleExample);
            //判断是否修改
            if (!authUserRole.getAuthRoleId().equalsIgnoreCase(updateReq.getAuthRoleId())){
                authUserRole.setAuthRoleId(updateReq.getAuthRoleId());
                //更新
                authUserRoleRepository.updateByPrimaryKey(authUserRole);
            }
        }
    }

    /**
      * 用户注册
      *
      * @date 2021/4/6
      * @param addReq 用户注册
      */
    @Override
    public void register(AuthUserAddReq addReq){
        addItem(addReq);
    }

    /**
     * 新增用户

     * @date 2019/8/31
     * @param addReq
     * @param addReq 是否需要发送短信
     * @return AuthUser
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public AuthUser addItem(AuthUserAddReq addReq) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>新增用户,参数:{}",JSON.toJSONString(addReq));
        //用户已经存在了就不开户
        Example authUserExample = new Example(AuthUser.class);
        Example.Criteria authUserCriteria = authUserExample.createCriteria();
        CriteriaBuilder.rigidCriteria(authUserCriteria);
        //独立OR查询条件
        Example.Criteria criteria = authUserExample.createCriteria();
        if(!CheckParam.isNull(addReq.getUserName())){
            criteria.orEqualTo("userName", addReq.getUserName());
        }
        if(!CheckParam.isNull(addReq.getAuthUserId())){
            criteria.orEqualTo("authUserId", addReq.getAuthUserId());
        }
        authUserExample.and(criteria);
        AuthUser existedAuthUser = authUserRepository.selectOneByExample(authUserExample);
        if (!CheckParam.isNull(existedAuthUser)) {
            if(!CheckParam.isNull(addReq.getUserName())){
                existedAuthUser.setUserName(addReq.getUserName());
            }
            if(!CheckParam.isNull(addReq.getRealName())){
                existedAuthUser.setRealName(addReq.getRealName());
            }
            authUserRepository.updateByPrimaryKeySelective(existedAuthUser);
            return existedAuthUser;
        }
        AuthUser authUser = new AuthUser();
        String authUserId = authUser.getAuthUserId();
        addReq.setAuthUserId(authUserId);
        Integer lockStatus = addReq.getLockStatus();
        if(CheckParam.isNull(lockStatus)){
            authUser.setLockStatus(AuthUserLockStatus.LOCKED.getCode());
        }else{
            authUser.setLockStatus(lockStatus);
        }
        //用户密码盐
        String authUserSalt = SnowflakeIdWorker.uniqueSequenceStr();
        String password = addReq.getPassword();
        if(CheckParam.isNull(password)){
            password = baseConstant.getDefaultPassword();
        }
        String encryptedPassword = EncryptUtils.encrypt(password, authUserSalt);
        authUser.setAuthPassword(encryptedPassword);
        authUser.setAuthSalt(authUserSalt);
        authUser.setUserName(addReq.getUserName());
        authUser.setDepartmentId(addReq.getDepartmentId());
        authUser.setRealName(addReq.getRealName());
        authUser.setPhoneNumber(addReq.getPhoneNumber());
        try {
            BaseUtil.setFieldValueNotNull(authUser);
        } catch (Exception e) {
            log.error("新增用户->设置用户为空的属性失败 {} , {} ",e.getMessage(),e);
        }
        authUserRepository.insert(authUser);
        //角色特殊处理
        AuthUserRole authUserRole = new AuthUserRole();
        authUserRole.setAuthRoleId(addReq.getAuthRoleId());
        authUserRole.setAuthUserId(authUser.getAuthUserId());
        authUserRoleRepository.insert(authUserRole);
        //初始化用户默认配置
        AuthUserConfig authUserConfig = new AuthUserConfig();
        authUserConfig.setAuthUserId(authUser.getAuthUserId());
        authUserConfig.setColor(PlatformConstant.DEFAULT_USER_CONFIG_COLOR);
        authUserConfig.setFixHeader(PlatformConstant.DEFAULT_USER_FIX_HEADER);
        authUserConfig.setFixSiderBar(PlatformConstant.DEFAULT_USER_FIX_SIDE_BAR);
        authUserConfig.setLayout(PlatformConstant.DEFAULT_USER_LAYOUT);
        authUserConfig.setMultiPage(PlatformConstant.DEFAULT_USER_MULTI_PAGE);
        authUserConfig.setTheme(PlatformConstant.DEFAULT_USER_THEME);
        authUserConfigRepository.insert(authUserConfig);
        List<UserSketchReq> sketchList = addReq.getSketchList();
        insertUserSketchList(authUserId,sketchList);
        return authUser;
    }


    /**
      * 插入用户的简略信息
      *
      * @date 2021/5/25
      * @param authUserId 用户ID
      * @param sketchList 简略信息集合
      */
    @Override
    public void insertUserSketchList(String authUserId, List<UserSketchReq> sketchList){
        if(CollectionUtils.isEmpty(sketchList)){
            return;
        }
        sketchList.stream().forEach(item -> {
            insertUserSketch(authUserId,item);
        });
    }

    /**
      * 设置用户的简略信息
      *
      * @date 2021/5/8
      * @param sketchReq
      */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void insertUserSketch(String authUserId,UserSketchReq sketchReq){
        log.info(">>>>>>>>>>>>>>>>>>>>>执行插入用户简略信息,当前线程ID:{}<<<<<<<<<<<<<<<<<<<",Thread.currentThread().getId());
        String sketchOtherId = sketchReq.getSketchOtherId();
        String sketchOtherType = sketchReq.getSketchOtherType().toString();
        Example  authUserExample = Example.builder(AuthUserSketch.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("authUserId", authUserId)
                .andEqualTo("sketchOtherType", sketchOtherType))
                .build();
        AuthUserSketch authUserSketch = authUserSketchRepository.selectOneByExample(authUserExample);
        if(!CheckParam.isNull(authUserSketch)){
            authUserSketchRepository.delete(authUserSketch);
        }
        //地区ID和用户ID都不为空的时候生成用户简略信息 生成的是地区信息
        if(!CheckParam.isNull(sketchOtherId)){
            //生成用户简略信息
            authUserSketch = new AuthUserSketch();
            authUserSketch.setAuthUserId(authUserId);
            authUserSketch.setSketchOtherId(sketchOtherId);
            authUserSketch.setSketchOtherType(sketchOtherType);
            try {
                BaseUtil.setFieldValueNotNull(authUserSketch);
            } catch (Exception e) {
                log.error("新增->设置设置用户的简略信息为空的属性失败 {} , {} ",e.getMessage(),e);
            }
            authUserSketchRepository.insert(authUserSketch);
        }
    }

    /**
     * 新增用户
     * @author
     * @date 2019/8/31
     * @param addReq
     * @return AuthUser
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public AuthUser addNewAuthUser(AuthUserAddReq addReq) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>新增用户,参数:{}",JSON.toJSONString(addReq));
        AuthUser authUser = addItem(addReq);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>新增用户,结果:{}",JSON.toJSONString(authUser));
        return authUser;
    }

    /**
      * 删除用户

      * @date 2019/9/2
      * @param mainIdList id集合
      */
    @Override
    public void batchDeleteItem(String mainIdList) {
        List<String> authUserIdList = Arrays.asList(mainIdList.split(","));
        if(!CollectionUtils.isEmpty(authUserIdList)){
            log.info(">>>>>>>>>>>>>>>>正在删除用户:{}<<<<<<<<<<<<<<<<<<<<",JSON.toJSONString(authUserIdList));
            authUserRepository.deleteByAuthUserIdList(authUserIdList);
            authUserRoleRepository.deleteByAuthUserIdList(authUserIdList);
            authUserSketchRepository.deleteByAuthUserIdList(authUserIdList);
            authUserIdList.forEach(userId->{
                authUserLogOut(userId); //执行登出
            });
        }
    }

    /**
     * 重置密码

     * @date 2019/9/2
     * @param userNameList 用户名集合
     */
    @Override
    public void resetPassword(String userNameList) {
        String[] split = userNameList.split(",");
        Arrays.stream(split).forEach(userName->{
           log.info(">>>>>>>>>>>>>>>>正在重置密码:{}<<<<<<<<<<<<<<<<<<<<<",userName);
            String authSalt = SnowflakeIdWorker.uniqueSequenceStr();
            authUserRepository.resetPassword(userName,EncryptUtils.encrypt(baseConstant.getDefaultPassword(),authSalt),authSalt);
        });
    }


}
