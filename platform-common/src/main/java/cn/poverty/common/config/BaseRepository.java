package cn.poverty.common.config;

import tk.mybatis.mapper.common.*;

/**
 * 继承通用的mapper，关键点就是这个接口不能被扫描到，不能跟dao这个存放mapper文件放在一起。
 
 * @time 2018/10/12
 * @description
 */
public interface BaseRepository <T> extends Mapper<T>, BaseMapper<T>, MySqlMapper<T>, IdsMapper<T>, ConditionMapper<T>,ExampleMapper<T> {












}
