package cn.poverty.common.utils;

import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.function.CacheSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 基本的查询Repository
 * @date 2019-08-20
 */
@Slf4j
public class BaseQueryRepository {



    /**
     * 通过函数式查询的方式查询数据缓存查询
     * @param cacheSelector    查询缓存的方法
     * @param databaseSelector 数据库查询方法
     * @return T
     */
    public static <T> T queryByFunctional(CacheSelector<T> cacheSelector, Supplier<T> databaseSelector) {
        try {
            log.debug(">>>>>>>>>>>>>>>>>>>>>>query data from redis<<<<<<<<<<<<<<<<");
            // 先查 Redis缓存
            T t = cacheSelector.query();
            if (CheckParam.isNull(t)) {
                // 没有记录再查询数据库
                T dbQueryResult = databaseSelector.get();
                return dbQueryResult;
            } else {
                return t;
            }
        } catch (Exception e) {
            // 缓存查询出错，则去数据库查询
            log.error("redis error："+e.getMessage(), e);
            log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>query data from database<<<<<<<<<<<<<<<<");
            return databaseSelector.get();
        }
    }
}
