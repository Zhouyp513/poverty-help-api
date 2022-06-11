package cn.poverty.service.config.shiro;

import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.BaseQueryRepository;
import cn.poverty.common.utils.auth.AuthEncrypt;
import cn.poverty.common.utils.common.BaseUtil;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.spring.HttpContextUtil;
import cn.poverty.repository.repository.AuthRoleRepository;
import cn.poverty.repository.repository.AuthUserRepository;
import cn.poverty.repository.result.AuthPermissionQueryResult;
import cn.poverty.service.AuthCacheService;
import cn.poverty.common.constants.AuthConstants;
import cn.poverty.repository.result.AuthRoleResult;
import cn.poverty.repository.result.AuthUserResult;
import cn.poverty.common.constants.BaseConstant;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: Shiro的域
 * @date 2019-08-20
 */
public class ShiroAuthorizationRealm extends AuthorizingRealm {

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private AuthCacheService authCacheService;

    @Resource
    private AuthUserRepository authUserRepository;

    @Resource
    private AuthRoleRepository authRoleRepository;

    @Resource
    private BaseConstant baseConstant;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权模块，拿到用户角色和权限
     * @param token token
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {


        //当前用户用户名
        String userName = JwtUtil.getUserName(String.valueOf(token));

        //需要返回的对象
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //当前用户的角色
        List<AuthRoleResult> roleList = BaseQueryRepository.queryByFunctional(
                () -> authCacheService.getRoles(userName),
                () -> authRoleRepository.queryUserRoleDataByUserName(baseConstant.getUnDeleteStatus(), userName));

        if(!CheckParam.isNull(roleList) && !roleList.isEmpty()){
            // 拿到用户角色集
            simpleAuthorizationInfo.setRoles(roleList.stream().map(AuthRoleResult::getRoleName).collect(Collectors.toSet()));
        }

        // 拿到用户权限集
        List<AuthPermissionQueryResult> permissionList = BaseQueryRepository.queryByFunctional(
                () -> authCacheService.getPermissions(userName),
                () -> authUserRepository.queryAuthUserPermissionByUserName(baseConstant.getUnDeleteStatus(), userName));


        if(!CheckParam.isNull(permissionList) && !permissionList.isEmpty()){
                //此处此处权限值需要排除空
                simpleAuthorizationInfo.setStringPermissions(permissionList.stream().filter(n1 -> !CheckParam.isNull(n1.getPerms())).map(AuthPermissionQueryResult::getPerms).collect(Collectors.toSet()));
        }


        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     * @param authenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 这里的 token是从 JWTFilter 的 executeLogin 方法传递过来的，需要进行解密
         String token = (String) authenticationToken.getCredentials();

        // 从 redis里拿到这个 token
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = BaseUtil.getIpAddrByHttpServletRequest(request);

        //将JWT里面的token进行加密
        String encryptTokenFromJwt = AuthEncrypt.encryptToken(token);
        String tokenInRedis  = redisRepository.get(AuthConstants.AUTHENTICATED_TOKEN + encryptTokenFromJwt);

        // 如果找不到，说明已经失效
        if (CheckParam.isNull(tokenInRedis)) {
            throw new AuthenticationException("token已经过期");
        }


        //通过JWT的解密拿到当前用户的Token
        String userName = JwtUtil.getUserName(token);

        if (CheckParam.isNull(userName)) {
            throw new AuthenticationException("token校验不通过");
        }

        //使用函数式查询方式查询数据，先查询缓存如果查询不到再查询数据库 如果是从缓存里面查询的话就直接查询Redis
        AuthUserResult authUserResult = BaseQueryRepository.queryByFunctional(
                () -> authCacheService.queryUserCacheByKey(tokenInRedis,AuthUserResult.class),
                () -> authUserRepository.queryAuthUserSketchByUserName(baseConstant.getUnDeleteStatus(),
                        userName));

        if (CheckParam.isNull(authUserResult)) {
            throw new AuthenticationException("用户名或密码错误");
        }

        if(!encryptTokenFromJwt.equalsIgnoreCase(tokenInRedis)){
            throw new AuthenticationException("token校验不通过");
        }
       /* if (!JWTUtil.verify(token, userName, authUserResult.getPassword()))*/
        return new SimpleAuthenticationInfo(token, token, "shiro_realm");
    }
}
