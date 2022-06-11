package cn.poverty.common.redis;

import cn.poverty.common.utils.common.CheckParam;
import cn.hutool.core.util.StrUtil;
import cn.poverty.common.utils.collection.CollectionUtils;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作封装
 
 * @time 2018/10/11
 * @description
 */
@Service("redisRepository")
public class RedisRepository {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
      * 匹配所有的字符串
      */
    private static final String ALL_MATCH_STR = "*";

    /**
     * 根据key存储Redis的Set结构
     *
     * @date 2019-05-29
     * @param key 键
     * @return Set
     */
    public Set<String> getSetByKey(String key){
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * 根据key存储Redis的Set结构
     *
     * @date 2019-05-29
     * @param key 键
     * @param keyList 值的集合
     * @return Long
     */
    public Long setSetByKey(String key, Set<String> keyList){
        stringRedisTemplate.opsForSet().members("");
        return stringRedisTemplate.opsForSet().add(key, keyList.toArray(new String[keyList.size()]));
    }

    /**
     * 使用spring-info-redis实现incr自增
     * @param key 存储的Key
     * @param liveTime 存活时间
     * @return
     */
    public Long incrAndExpire(String key, long liveTime) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, stringRedisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        //初始设置过期时间
        if (liveTime > 0) {
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }
        return increment;
    }

    /**
     * 使用spring-info-redis实现incr递减
     * @param key 存储的Key
     * @return
     */
    public Long decr(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, stringRedisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndDecrement();
        //初始设置过期时间
        if ((null == increment || increment.longValue() == 0)) {
            entityIdCounter.decrementAndGet();
        }
        return increment;
    }

    /**
     * 使用spring-info-redis实现incr自增
     * @param key 存储的Key
     * @return
     */
    public Long incr(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, stringRedisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        //初始设置过期时间
        if ((null == increment || increment.longValue() == 0)) {
            entityIdCounter.incrementAndGet();
        }

        return increment;
    }

    /**
     * 使用spring-info-redis实现incr递减
     * @param key 存储的Key
     * @param liveTime 存活时间
     * @return
     */
    public Long decrAndExpire(String key, long liveTime) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, stringRedisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndDecrement();
        //初始设置过期时间
        if (liveTime > 0) {
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }
        return increment;
    }

    /**
     * 疯转Redis的get操作
     * @param key
     * @return
     */
    public String get(Object key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 模糊查询Redis的Key
     *
     * @date 2019-08-23
     * @param pattern 模糊匹配的字符串
     * @return
     */
    public HashSet<String> keys(String pattern) {
        Set<String> keys = stringRedisTemplate.keys("*" + pattern + "*");
        if(!CheckParam.isNull(keys) && !keys.isEmpty()){
            HashSet valueSet = new HashSet();
            keys.forEach(k -> {
                String value = stringRedisTemplate.opsForValue().get(k);
                if(!CheckParam.isNull(value)){
                    valueSet.add(value);
                }
            });
            return valueSet;
        }
        return new HashSet<>();
    }

    /**
     * 设置值
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key,String value,Long liveTime){
        stringRedisTemplate.opsForValue().set(key,value,liveTime,TimeUnit.SECONDS);
    }

    /**
     * 设置值
     * @param key
     * @param value
     * @param liveTime
     * @param timeUnit 存活的时间单位
     */
    public void set(String key,String value,Long liveTime,TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key,value,liveTime,timeUnit);
    }

    /**
     * 设置值
     * @param key
     * @param value
     */
    public void set(String key,String value){
        stringRedisTemplate.opsForValue().set(key,value);
    }

    /**
     * 删除值
     * @param key
     */
    public void delete(String key){
        stringRedisTemplate.delete(key);
    }

    /**
     * 一次性添加list
     
     * @date 2019/12/4
     * @param
     * @return
     */
    public Long leftPushAll(String key, List<String> amountList){
        return stringRedisTemplate.opsForList().leftPushAll(key, amountList);
    }

    /**
     * 单个左边添加
     
     * @date 2019/12/4
     * @param
     * @return
     */
    public Long leftPush(String key, String amount){
        return stringRedisTemplate.opsForList().leftPush(key, amount);
    }

    /**
     * 对队列左边拿到第一个
     
     * @date 2019/12/4
     * @param
     * @return
     */
    public String leftPop(String key){
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    /**
     * 一次性添加list
     
     * @date 2019/12/4
     * @param
     * @return
     */
    public Long rightPushAll(String key, List<String> amountList){
        return stringRedisTemplate.opsForList().rightPushAll(key, amountList);
    }

    /**
     * 单个右边边添加
     
     * @date 2019/12/4
     * @param
     * @return
     */
    public Long rightPush(String key, String amount){
        return stringRedisTemplate.opsForList().rightPush(key, amount);
    }

    /**
     * 对队列左边拿到第一个
     
     * @date 2019/12/4
     * @param
     * @return
     */
    public String rightPop(String key){
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * 拿到多个位置缓存对象的经纬度坐标点
     
     * @date 2019/11/26
     * @param key
     * @param names
     * @return
     */
    public Point getOnePos(String key,String... names){
        List<Point> position = stringRedisTemplate.opsForGeo().position(key, names);
        return position.get(0);
    }


    /**
      * 新增一个经纬度
      *
      * @date 2021/7/22
      * @param key 缓存的经纬度KEY
      * @param longitude 经度
      * @param latitude 纬度
      * @param memberName 经纬度坐标标记名称
      */
    public void addGeo(String key,Double longitude,Double latitude,String memberName){
        Long addedNum = stringRedisTemplate.opsForGeo()
                .add(key,new Point(longitude,latitude),memberName);
        System.out.println(addedNum);
    }

    /**
     * 拿到多个名称的位置信息
     
     * @date 2019/11/26
     * @param  names
     * @return java.util.List
     */
    public List<Point> getListPos(String key,String... names){
        List<Point> position = stringRedisTemplate.opsForGeo().position(key, names);
        return position;
    }


    /**
      * 计算两个坐标位置之间的距离
      *
      * @date 2021/7/22
      * @param  key 缓存key
      * @param  memberOne 位置缓存对象名称1
      * @param  memberTwo 位置缓存对象名称2
      * @return java.lang.Double
      */
    public Double getDistinct(String key,String memberOne,String memberTwo){
        Distance distance = stringRedisTemplate.opsForGeo()
                .distance(key,memberOne,memberTwo, RedisGeoCommands.DistanceUnit.METERS);
        return distance.getValue();
    }

     /**
     * 计算两个坐标位置之间的距离
     *
     * @date 2021/7/22
     * @param  key 缓存key
     * @param  memberOne 位置缓存对象名称1
     * @param  memberTwo 位置缓存对象名称2
     * @return org.springframework.data.geo.Distance
     */
    public Distance getTwoNameDistance(String key, String memberOne, String memberTwo){
        Distance distance =stringRedisTemplate.opsForGeo().distance(key, memberOne, memberTwo, Metrics.MILES);
        return distance;
    }

    /**
     * 根据给定地理位置坐标拿到指定范围内的地理位置集合
     
     * @date 2019/11/26
     * @param key
     * @param within  中心点和范围
     * @param args   返回个数和排序方式
     * @return
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> getPointRadius(String key,
                                                                           Circle within,
                                                                           RedisGeoCommands.GeoRadiusCommandArgs args) {
        GeoOperations<String, String> ops = stringRedisTemplate.opsForGeo();
        return args == null ? ops.radius(key, within) : ops.radius(key, within, args);
    }

    /**
     * 拿到某个地理位置的 geohash 值
     
     * @date 2019/11/26
     * @param
     * @return
     */
    public String getNameGeoHash(String key, String name) {
        List<String> hashList = stringRedisTemplate.opsForGeo().hash(key, name);
        return hashList.get(0);
    }

    /**
     * 删除位置信息
     
     * @date 2019/11/29
     * @param key 键
     * @param name 名称
     * @return Long
     */
    public Long deleteGeo(String key,String name){
        return stringRedisTemplate.opsForGeo().remove(key,name);
    }

    /**
      * 根据键查询所有key
      *
      * @date 2021/3/30
      * @param key 键
      * @return Set
      */
    public Set<String> queryByKeys(String key){
        return stringRedisTemplate.keys(StrUtil.wrap(key, ALL_MATCH_STR, ALL_MATCH_STR));
    }

    /**
     * 根据键删除所有key
     *
     * @date 2021/3/30
     * @param keyList 键集合
     * @return Set
     */
    public Long deleteByKeys(Set<String> keyList){
        if(CollectionUtils.isEmpty(keyList)){
            return stringRedisTemplate.delete(keyList);
        }
        return 0L;
    }

}
