package cn.poverty.service.impl;

import cn.poverty.common.constants.AuthConstants;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.repository.entity.AuthUser;
import cn.poverty.repository.entity.AuthUserConfig;
import cn.poverty.repository.repository.AuthPermissionRepository;
import cn.poverty.repository.repository.AuthRoleRepository;
import cn.poverty.repository.repository.AuthUserConfigRepository;
import cn.poverty.repository.repository.AuthUserRepository;
import cn.poverty.repository.repository.AuthUserRoleRepository;
import cn.poverty.repository.result.AuthPermissionQueryResult;
import cn.poverty.repository.result.AuthRoleResult;
import cn.poverty.repository.result.AuthUserResult;
import cn.poverty.service.AuthCacheService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.startup.UserConfig;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 权限缓存Service
 * @date 2019-08-20
 */
@Service(value = "authCacheService")
@Slf4j
public class AuthCacheServiceImpl implements AuthCacheService {

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private AuthUserRepository authUserRepository;

    @Resource
    private AuthPermissionRepository authPermissionRepository;

    @Resource
    private AuthRoleRepository authRoleRepository;

    @Resource
    private AuthUserRoleRepository authUserRoleRepository;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthUserConfigRepository authUserConfigRepository;

    /**
     * 从缓存里面拿到用户
     * @author
     * @date 2019-08-20
     * @param key 键
     * @param clz 类型
     * @return AuthUserResult
     * @throws Exception e
     */
    @Override
    public AuthUserResult queryUserCacheByKey(String key,Class<?> clz) throws Exception {
        String userString = redisRepository.get(AuthConstants.AUTHENTICATED_USER_META_TOKEN +  key);
        if (CheckParam.isNull(userString)) {
            return null;
        } else {
            return JSON.parseObject(userString, AuthUserResult.class);
        }
    }

    /**
      * 从缓存里面拿到用户角色信息
      * @author
      * @date 2019-08-20
      * @param userName 用户名
      * @return
      */
    @Override
    public List<AuthRoleResult> getRoles(String userName) throws Exception {
        String roleListString = redisRepository.get(AuthConstants.AUTHENTICATED_USER_ROLE + userName);
        if (CheckParam.isNull(roleListString)) {
            return null;
            //throw new Exception();
        } else {
            return JSON.parseArray(roleListString,AuthRoleResult.class);
        }
    }


    /**
     * 从缓存里面拿到菜单权限信息
     * @author
     * @date 2019-08-20
     * @param userName 用户名
     * @return
     */
    @Override
    public List<AuthPermissionQueryResult> getPermissions(String userName) throws Exception {
        String permissionListString = redisRepository.get(AuthConstants.AUTHENTICATED_USER_PERMISSION + userName);
        if (CheckParam.isNull(permissionListString)) {
            //throw new Exception();
            return null;
        } else {
            return JSON.parseArray(permissionListString,AuthPermissionQueryResult.class);
        }
    }


    /**
     * 从缓存里面拿到菜单权限信息
     * @author
     * @date 2019-08-20
     * @param userId 用户业务主键ID
     * @return
     */
    @Override
    public UserConfig getUserConfig(String userId) throws Exception {
        String userConfigString = redisRepository.get(AuthConstants.AUTHENTICATED_USER_CONFIG + userId);
        if (CheckParam.isNull(userConfigString)) {
            return null;
            //throw new Exception();
        } else {
            return JSON.parseObject(userConfigString,UserConfig.class);
        }
    }


    /**
     * 缓存用户权限信息权限信息
     * @author
     * @date 2019-08-20
     * @param userName 用户名称
     */
    @Override
    public void savePermissions(String userName) throws Exception {
        List<AuthPermissionQueryResult> permissionList = authUserRepository.queryAuthUserPermissionByUserName(baseConstant.getUnDeleteStatus(), userName);
        if (!permissionList.isEmpty()) {
            this.deletePermissions(userName);
            redisRepository.set(AuthConstants.AUTHENTICATED_USER_PERMISSION + userName, JSON.toJSONString(permissionList),baseConstant.getAuthExpiredTime(), TimeUnit.MINUTES);
        }
    }

    /**
     * 将用户角色和权限添加到 Redis缓存中
     * @author
     * @date 2019-09-05
     * @param  authUserIds 用户主键ID集合
     */
    @Override
    public void saveUserPermissionRoleRedisCache(List<String> authUserIds) {

        Example authUserExample = Example.builder(AuthUser.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andIn("authUserId", authUserIds))
                .build();

        List<AuthUser> authUserList = authUserRepository.selectByExample(authUserExample);

        if(!CollectionUtils.isEmpty(authUserList)) {
            authUserList.stream().forEach(u1 -> {
                try {
                    saveRoles(u1.getUserName());
                    savePermissions(u1.getUserName());
                } catch (Exception e) {
                    log.error("将用户角色和权限添加到 Redis缓存中失败 {} , {} ",e.getMessage(),e);
                }
            });
        }
    }

    /**
     * 保存角色
     * @author
     * @date 2019-08-20
     * @param userName 用户名称
     */
    @Override
    public void saveRoles(String userName) throws Exception {
        List<AuthRoleResult> authRoleResultList = authRoleRepository.queryUserRoleDataByUserName(baseConstant.getUnDeleteStatus(), userName);
        if (!authRoleResultList.isEmpty()) {
            this.deleteRoles(userName);
            redisRepository.set(AuthConstants.AUTHENTICATED_USER_ROLE + userName, JSON.toJSONString(authRoleResultList),baseConstant.getAuthExpiredTime(),TimeUnit.MINUTES);
        }
    }

    /**
     * 根据用户名删除角色
     * @author
     * @date 2019-08-20
     * @param userName 用户名称
     */
    @Override
    public void deleteRoles(String userName) throws Exception {
        userName = userName.toLowerCase();
        redisRepository.delete(AuthConstants.AUTHENTICATED_USER_ROLE + userName);
    }

    /**
     * 根据用户名删除权限
     * @author
     * @date 2019-08-20
     * @param userName 用户名称
     */
    @Override
    public void deletePermissions(String userName) throws Exception {
        userName = userName.toLowerCase();
        redisRepository.delete(AuthConstants.AUTHENTICATED_USER_PERMISSION + userName);
    }

    /**
     * 根据用户名删除权限
     * @author
     * @date 2019-08-20
     * @param userId 用户ID
     */
    @Override
    public void deleteUserConfigs(String userId) throws Exception {
        redisRepository.delete(AuthConstants.AUTHENTICATED_USER_CONFIG + userId);
    }

    /**
     * 保存用户配置
     * @author
     * @date 2019-08-20
     * @param userId 用户ID
     */
    @Override
    public void saveUserConfigs(String userId) throws Exception {
        Example authUserConfigExample = Example.builder(AuthUserConfig.class).where(Sqls.custom().andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus()).andEqualTo("userId", userId)).build();
        AuthUserConfig authUserConfig = authUserConfigRepository.selectOneByExample(authUserConfigExample);

        if (CheckParam.isNull(authUserConfig)) {
            deleteUserConfigs(userId);
            redisRepository.set(AuthConstants.AUTHENTICATED_USER_CONFIG + userId, JSON.toJSONString(authUserConfig),baseConstant.getAuthExpiredTime(),TimeUnit.MINUTES);
        }
    }
}
