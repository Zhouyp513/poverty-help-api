package cn.poverty.common.config;

import cn.poverty.common.constants.RedisConstants;
import cn.poverty.common.context.ApplicationContextHolder;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.collection.CollectionUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 
 * @packageName cn.poverty.common.config
 * @Description: Redis缓存配置
 * @date 2019-05-30
 */
@Slf4j
public class RedisEntityCacheHandler implements Cache {

    /**
     * redis读写锁
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * cache instance id 缓存实例的ID
     */
    private final String id;

    /**
     * redisTemplate
     */
    private RedisTemplate redisTemplate;

    /**
     * redisRepository
     */
    private RedisRepository redisRepository;

    /**
     * redis过期时间 -> 3分钟
     */
    private static final long EXPIRE_TIME_IN_MINUTES = 3;



    public RedisEntityCacheHandler(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }
    @Override
    public String getId() {
        return id;
    }

    /**
     * Put query result to redis
     * @param key
     * @param value
     */
    @Override
    public void putObject(Object key, Object value) {
        String cacheKey = RedisConstants.REDIS_QUERY_CACHE_PREFIX+DigestUtils.md5Hex(String.valueOf(key));
        RedisTemplate redisTemplate = getRedisTemplate();
        ValueOperations opsForValue = redisTemplate.opsForValue();
        String jsonValue = JSON.toJSONString(value);
        opsForValue.set(cacheKey,
                jsonValue, RedisConstants.EXPIRE_TIME_IN_SECONDS, TimeUnit.SECONDS);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>放入结果到缓存: key: {},value:{} <<<<<<<<<<<<<<<<<<<",cacheKey,jsonValue);
    }

    /**
     * Get cached query result from redis
     * @param key
     * @return
     */
    @Override
    public Object getObject(Object key) {
        String cacheKey = RedisConstants.REDIS_QUERY_CACHE_PREFIX+DigestUtils.md5Hex(String.valueOf(key));
        RedisTemplate redisTemplate = getRedisTemplate();
        ValueOperations opsForValue = redisTemplate.opsForValue();
        Object resultObject = opsForValue.get(RedisConstants.REDIS_QUERY_CACHE_PREFIX + cacheKey);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>拿到缓存结果: key: {},value:{} <<<<<<<<<<<<<<<<<<<",cacheKey,JSON.toJSON(resultObject));
        return resultObject;
    }

    /**
     * Remove cached query result from redis
     * @param key
     * @return
     */
    @Override
    public Object removeObject(Object key) {
        String cacheKey = RedisConstants.REDIS_QUERY_CACHE_PREFIX+DigestUtils.md5Hex(String.valueOf(key));
        RedisTemplate redisTemplate = getRedisTemplate();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>从二级缓存删除数据,需要被删除的key:{}<<<<<<<<<<<<<<<<<<<",cacheKey);
        return redisTemplate.delete(cacheKey);
    }

    /**
     * Clears this cache instance
     */
    @Override
    public void clear() {
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.execute((RedisCallback) connection -> {
            RedisRepository redisRepository = returnRedisRepository();
            Set<String> keyList = redisRepository.queryByKeys(RedisConstants.REDIS_QUERY_CACHE_PREFIX);
            log.info(">>>>>>>>>>>>>>>需要被删除的缓存集合:{}<<<<<<<<<<<<",JSON.toJSONString(keyList));
            if(!CollectionUtils.isEmpty(keyList)){
                return redisRepository.deleteByKeys(keyList);
            }
            return 0L;
        });
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>清空二级缓存数据<<<<<<<<<<<<<<<<<<<");
    }
    @Override
    public int getSize() {
        return 0;
    }
    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    /**
     * 拿到RedisTemplate
     * 
     * @date 2019-05-30
     * @return RedisTemplate
     */
    private RedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = ApplicationContextHolder.getBean("redisTemplate");
        }
        return redisTemplate;
    }

    /**
     * 拿到RedisTemplate
     * 
     * @date 2019-05-30
     * @return RedisTemplate
     */
    private RedisRepository returnRedisRepository() {
        if (redisRepository == null) {
            redisRepository = ApplicationContextHolder.getBean("redisRepository");
        }
        return redisRepository;
    }

}
