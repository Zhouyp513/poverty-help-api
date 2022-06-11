package cn.poverty.repository.repository;

import cn.poverty.common.config.BaseRepository;
import cn.poverty.common.config.RedisEntityCacheHandler;
import cn.poverty.repository.entity.AuthPermission;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;


/**
 * 权限Repository
 * @title: AuthPermissionRepository.java
 
 * @date 2019/4/24 11:21
 * @return
 */
@Mapper
@CacheNamespace(implementation = RedisEntityCacheHandler.class,flushInterval = 60000)
public interface AuthPermissionRepository extends BaseRepository<AuthPermission> {



}
