package cn.poverty.common.aspect;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 
 * @packageName cn.poverty.common.aspect
 * @Description: API日志
 * @date 2021-01-21
 */
@Aspect
@Component
@Slf4j
public class ApiAccessLogAspect {

    /**
     * 控制器切点,拦截DELETE请求
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteApiMethodAspect(){

    }

    /**
     * 返回通知
     * @param joinPoint
     * @param resp 此参数值必须和方法签名里面的resp一致
     */
    @AfterReturning(pointcut="deleteApiMethodAspect()",returning = "resp")
    public void afterReturnDeleteApiMethod(JoinPoint joinPoint, Object resp){
        handleLog(joinPoint,resp);
    }

    /**
     * 抛出通知
     * @param joinPoint
     * @param ex 此参数值必须和方法签名里面的ex一致
     */
    @AfterThrowing(pointcut="putApiMethodAspect()",throwing = "ex")
    public void afterThrowingDeleteApiMethod(JoinPoint joinPoint, Exception ex){
        handleLog(joinPoint,ex);
    }


    /**
     * 控制器切点,拦截PUT请求
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putApiMethodAspect(){

    }

    /**
     * 返回通知
     * @param joinPoint
     * @param resp 此参数值必须和方法签名里面的resp一致
     */
    @AfterReturning(pointcut="putApiMethodAspect()",returning = "resp")
    public void afterReturnPutApiMethod(JoinPoint joinPoint, Object resp){
        handleLog(joinPoint,resp);
    }

    /**
     * 抛出通知
     * @param joinPoint
     * @param ex 此参数值必须和方法签名里面的ex一致
     */
    @AfterThrowing(pointcut="putApiMethodAspect()",throwing = "ex")
    public void afterThrowingPutApiMethod(JoinPoint joinPoint, Exception ex){
        handleLog(joinPoint,ex);
    }


    /**
     * 控制器切点,拦截GET请求
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getApiMethodAspect(){

    }

    /**
     * 返回通知
     * @param joinPoint
     * @param resp 此参数值必须和方法签名里面的resp一致
     */
    @AfterReturning(pointcut="getApiMethodAspect()",returning = "resp")
    public void afterReturnGetApiMethod(JoinPoint joinPoint, Object resp){
        handleLog(joinPoint,resp);
    }

    /**
     * 抛出通知
     * @param joinPoint
     * @param ex 此参数值必须和方法签名里面的ex一致
     */
    @AfterThrowing(pointcut="getApiMethodAspect()",throwing = "ex")
    public void afterThrowingGetApiMethod(JoinPoint joinPoint, Exception ex){
        handleLog(joinPoint,ex);
    }


    /**
     * 控制器切点,拦截POST请求
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postApiMethodAspect(){

    }

    /**
     * 返回通知
     * @param joinPoint
     * @param resp 此参数值必须和方法签名里面的resp一致
     */
    @AfterReturning(pointcut="postApiMethodAspect()",returning = "resp")
    public void afterReturnPostApiMethod(JoinPoint joinPoint, Object resp){
        handleLog(joinPoint,resp);
    }

    /**
     * 抛出通知
     * @param joinPoint
     * @param ex 此参数值必须和方法签名里面的ex一致
     */
    @AfterThrowing(pointcut="postApiMethodAspect()",throwing = "ex")
    public void afterThrowingPostApiMethod(JoinPoint joinPoint, Exception ex){
        handleLog(joinPoint,ex);
    }




    /**
     * 控制器切点
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void controllerAspect(){

    }

    /**
     * 返回通知
     * @param joinPoint
     * @param resp 此参数值必须和方法签名里面的resp一致
     */
    @AfterReturning(pointcut="controllerAspect()",returning = "resp")
    public void afterReturnMethod(JoinPoint joinPoint, Object resp){
        handleLog(joinPoint,resp);
    }

    /**
     * 抛出通知
     * @param joinPoint
     * @param ex 此参数值必须和方法签名里面的ex一致
     */
    @AfterThrowing(pointcut="controllerAspect()",throwing = "ex")
    public void afterThrowingMethod(JoinPoint joinPoint, Exception ex){
        handleLog(joinPoint,ex);
    }

    /**
     * 处理日志
     * @param joinPoint
     * @param object
     */
    public void handleLog(JoinPoint joinPoint, Object object){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        StringBuilder params = new StringBuilder();
        if (args != null && paramNames != null) {
            try {
                params = handleParams(params, args, Arrays.asList(paramNames));
            } catch (JsonProcessingException e) {

                log.info(">>>>>>>>>>>>解析接口参数出现异常 : <<<<<<<<<<<<<"+e.getMessage(),e);
            }
        }

        log.info(">>>>>>>>>>>>>>>>>>接口请求参数<<<<<<<<<<<<<:"+params);

        //log.info("ARGS : " + JSON.toJSONString(joinPoint.getArgs()));
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
