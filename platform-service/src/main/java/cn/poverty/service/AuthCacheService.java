package cn.poverty.service;

import cn.poverty.repository.result.AuthPermissionQueryResult;
import cn.poverty.repository.result.AuthRoleResult;
import cn.poverty.repository.result.AuthUserResult;
import org.apache.catalina.startup.UserConfig;

import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 权限缓存配置
 * @date 2019-08-20
 */
 public interface AuthCacheService {


    /**
     * 将用户角色和权限添加到 Redis缓存中
     *
     * @date 2019-09-05
     * @param  authUserIds 用户主键ID集合
     */
    void saveUserPermissionRoleRedisCache(List<String> authUserIds);

    /**
     * 从缓存里面拿到用户
     *
     * @date 2019-08-20
     * @param userName 用户名
     * @param clz 类型
     * @return AuthUserResult
     * @throws Exception e
     */
    AuthUserResult queryUserCacheByKey(String userName,Class<?> clz) throws Exception;


    /**
     * 从缓存里面拿到用户角色信息
     *
     * @date 2019-08-20
     * @param userName 用户名
     * @return List
     * @throws Exception e
     */
     List<AuthRoleResult> getRoles(String userName) throws Exception;


    /**
     * 从缓存里面拿到菜单权限信息
     *
     * @date 2019-08-20
     * @param userName 用户名
     * @return List
     * @throws Exception e
     */
     List<AuthPermissionQueryResult> getPermissions(String userName) throws Exception;

    /**
     * 从缓存里面拿到菜单权限信息
     *
     * @date 2019-08-20
     * @param authUserId 用户业务主键ID
     * @return UserConfig
     * @throws Exception e
     */
     UserConfig getUserConfig(String authUserId) throws Exception;


    /**
     * 缓存用户权限信息权限信息
     *
     * @date 2019-08-20
     * @param userName 用户名称
     * @throws Exception e
     */
     void savePermissions(String userName) throws Exception;

    /**
     * 保存角色
     *
     * @date 2019-08-20
     * @param userName 用户名称
     * @throws Exception e
     */
     void saveRoles(String userName) throws Exception;


    /**
     * 根据用户名删除角色
     *
     * @date 2019-08-20
     * @param userName 用户名称
     * @throws Exception e
     */
     void deleteRoles(String userName) throws Exception;

    /**
     * 根据用户名删除权限
     *
     * @date 2019-08-20
     * @param userName 用户名称
     * @throws Exception e
     */
     void deletePermissions(String userName) throws Exception;


    /**
     * 根据用户名删除权限
     *
     * @date 2019-08-20
     * @param authUserId 用户ID
     * @throws Exception e
     */
     void deleteUserConfigs(String authUserId) throws Exception;


    /**
     * 保存用户配置
     *
     * @date 2019-08-20
     * @param authUserId 用户ID
     * @throws Exception e
     */
     void saveUserConfigs(String authUserId) throws Exception;

}
