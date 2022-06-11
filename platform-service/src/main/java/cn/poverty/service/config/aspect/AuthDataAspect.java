/*
package cn.poverty.service.config.aspect;

import cn.poverty.common.enums.AuthDataParamNameEnum;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.interaction.internal.auth.AuthUserMeta;
import cn.poverty.service.AuthUserService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

*/
/**
 
 * @packageName cn.poverty.common.aspect.data
 * @Description: 数据权限切面
 * @date 2021-05-10
 *//*

@Aspect
@Component
@Slf4j
public class AuthDataAspect {


    @Resource
    private AuthUserService authUserService;

    */
/**
     * 控制器切点,拦截POST请求
     *//*

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postApiMethodAspect(){

    }

    */
/**
     * 控制器切点,拦截PUT请求
     *//*

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putApiMethodAspect(){

    }



    */
/**
     * 环绕通知，处理切点里面的数据
     * 
     * @date 2019-08-29
     * @param point 环绕通知对象主要用于处理切点里面的数据
     * @return Object
     *//*

    @Around("postApiMethodAspect()")
    public Object postApiMethodAspectPointcut(ProceedingJoinPoint point) throws Throwable {
        handleDataParams(point);
        Object proceed = point.proceed();
        return proceed;
    }


    public void handleDataParams(ProceedingJoinPoint point){
        Object[] paramList = point.getArgs();
        if(null == paramList || paramList.length <= 0){
            return;
        }

        MethodSignature msg = (MethodSignature)point.getSignature();
        String[] paramName = msg.getParameterNames();
        List<String> paramNameList = Arrays.asList(paramName);

        AuthUserMeta authUserMeta = authUserService.currentUserCacheMeta();
        String regionId = authUserMeta.getRegionId();
        String corpBaseDataId = authUserMeta.getCorpBaseDataId();

        Arrays.asList(paramList).forEach(item -> {
            //设置地区字段
            if(StrUtil.equalsIgnoreCase(String.valueOf(item), AuthDataParamNameEnum.REGION_ID.getCode())){
                Class<?> aClass = item.getClass();
                if(CheckParam.isNull(regionId)){

                }
            }
        });

    }

}
*/
