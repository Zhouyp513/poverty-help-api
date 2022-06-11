package cn.poverty.service.config.shiro;

import cn.poverty.common.redis.RedisRepository;
import cn.poverty.repository.repository.AuthRoleRepository;
import cn.poverty.repository.repository.AuthUserRepository;
import cn.poverty.service.AuthCacheService;
import cn.poverty.common.constants.BaseConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * @author
 * @projectName poverty-help-api
 * @Description:
 * @date 2019-09-09
 */
@Slf4j
public class AppAuthorizationRealm extends AuthorizingRealm {

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

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println(principals);
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println(token);
        return null;
    }
}
