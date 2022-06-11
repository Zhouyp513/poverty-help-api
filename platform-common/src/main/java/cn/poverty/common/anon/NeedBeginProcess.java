package cn.poverty.common.anon;



import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 
 * @packageName cn.poverty.common.anon
 * @Description: 需要开始流程的注解 注解在业务方法上面
 * @date 2021-05-23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER})
public @interface NeedBeginProcess {


    /**
     * 需要插入的表的类名
     */
    Class<?> type();


    /**
     * 是否需要开启流程
     */
    boolean needBegin() default false;

}
