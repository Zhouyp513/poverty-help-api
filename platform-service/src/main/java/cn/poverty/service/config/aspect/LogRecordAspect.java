package cn.poverty.service.config.aspect;



import cn.poverty.common.utils.common.BaseUtil;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.interaction.internal.auth.AuthUserMeta;
import cn.poverty.repository.entity.SystemApiLog;
import cn.poverty.repository.repository.SystemApiLogRepository;
import cn.poverty.service.AuthUserService;
import cn.poverty.common.anon.ApiLog;
import cn.poverty.common.constants.BaseConstant;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

/**
 * AOP 记录用户操作日志
 *
 * @author MrBird
 * @link https://mrbird.cc/Spring-Boot-AOP%20log.html
 */
@Slf4j
@Aspect
@Component
public class LogRecordAspect {


    @Resource
    private SystemApiLogRepository systemApiLogRepository;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthUserService authUserService;


    /**
      * 定义切点
      * 
      * @date 2019-08-29
      */
    @Pointcut("@annotation(cn.poverty.common.anon.ApiLog)")
    public void pointcut() {
    }


    /**
      * 环绕通知，处理切点里面的数据
      * 
      * @date 2019-08-29
      * @param point 环绕通知对象主要用于处理切点里面的数据
      * @return Object
      */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 设置 IP 地址
        String ipAddress = BaseUtil.getIpAddress(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        // 保存日志
        String token = (String) SecurityUtils.getSubject().getPrincipal();

        AuthUserMeta authUserMeta = authUserService.currentUserMeta(false);

        SystemApiLog log = new SystemApiLog();

        if(!CheckParam.isNull(authUserMeta)){
            log.setUserName(authUserMeta.getUserName());
        }else{
            log.setUserName("");
        }

        log.setRequestIp(ipAddress);

        log.setTime(time);

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        ApiLog logAnnotation = method.getAnnotation(ApiLog.class);
        if (logAnnotation != null) {
            // 注解上的描述
            log.setOperation(logAnnotation.value());
        }
        // 请求的类名
        String className = point.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = signature.getName();
        log.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = point.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            log.setParams(params.toString());
        }
        log.setCreateTime(LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone()));

        log.setLocation(ipAddress);
        systemApiLogRepository.insert(log);
        return result;
    }




    /**
      * 拿到请求参数
      * 
      * @date 2019-08-29
      * @param params 要返回的参数
      * @param args 请求参数列表
      * @param paramNames 请求参数名称列表
      * @return StringBuilder
      */
    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) throws JsonProcessingException {
        for (int n1 = 0; n1 < args.length; n1++) {
            if (args[n1] instanceof Map) {
                Set set = ((Map) args[n1]).keySet();
                List<Object> list = new ArrayList<>();
                List<Object> paramList = new ArrayList<>();
                for (Object key : set) {
                    list.add(((Map) args[n1]).get(key));
                    paramList.add(key);
                }
                return handleParams(params, list.toArray(), paramList);
            } else {
                if (args[n1] instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) args[n1];
                    params.append(" ").append(paramNames.get(n1)).append(": ").append(file.getName());
                } else if (args[n1] instanceof Serializable) {
                    Class<?> aClass = args[n1].getClass();
                    try {
                        aClass.getDeclaredMethod("toString", new Class[]{null});
                        // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法

                        params.append(" ").append(paramNames.get(n1)).append(": ").append(JSON.toJSONString(args[n1]));
                    } catch (NoSuchMethodException e) {
                        params.append(" ").append(paramNames.get(n1)).append(": ").append(JSON.toJSONString(args[n1]));
                    }
                } else {
                    params.append(" ").append(paramNames.get(n1)).append(": ").append(args[n1]);
                }
            }
        }
        return params;
    }
}
