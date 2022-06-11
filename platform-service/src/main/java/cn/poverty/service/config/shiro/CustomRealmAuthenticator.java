package cn.poverty.service.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 自定义的认证器
 * @date 2019-09-09
 */
@Slf4j
public class CustomRealmAuthenticator extends ModularRealmAuthenticator {


    /**
      * 认证
      *
      * @date 2021/3/30
      * @param authenticationToken 认证所需包装的token
      * @return AuthenticationInfo
      */
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        log.info("UserModularRealmAuthenticator:method doAuthenticate() execute ");
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
        JwtToken userToken = (JwtToken) authenticationToken;
        // 登录类型
        String virtualType = userToken.getApiAccessType();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        Collection<Realm> typeRealms = new ArrayList<>();
        for (Realm realm : realms) {
            // 注：这里使用类名包含枚举，区分realm
            if (realm.getName().contains(virtualType)){
                typeRealms.add(realm);
            }
        }
        // 判断是单Realm还是多Realm
        if (typeRealms.size() == 1) {
            log.info("doSingleRealmAuthentication() execute ");
            return doSingleRealmAuthentication(typeRealms.iterator().next(), userToken);
        } else {
            log.info("doMultiRealmAuthentication() execute ");
            return doMultiRealmAuthentication(typeRealms, userToken);
        }
    }
}
