package cn.poverty.common.function;


/**
 * 缓存数据查询函数式接口
 
 * @time 2017/11/22
 */
@FunctionalInterface
public interface CacheSelector<T> {

    /**
     * 缓存查询方法
     * 
     * @date 2021/3/29
     * @return T
     * @throws Exception e 需要抛出异常
     */
    T query() throws Exception;
}
