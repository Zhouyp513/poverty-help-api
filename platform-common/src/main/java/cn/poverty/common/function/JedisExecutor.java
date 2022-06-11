package cn.poverty.common.function;

/**
 * JedisExecutor Jedis的执行注解
 
 * @packageName cn.poverty.common.function
 * @Description: JedisExecutor Jedis的执行注解
 * @date 2021-01-21
 */
@FunctionalInterface
public interface JedisExecutor<T, R> {

    /**
     * 执行方法
     * 
     * @date 2021/3/29
     * @param t 参数
     * @return R
     */
    R excute(T t) ;
}
