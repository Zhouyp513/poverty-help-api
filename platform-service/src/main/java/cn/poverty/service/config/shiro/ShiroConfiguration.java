package cn.poverty.service.config.shiro;

import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.constants.BaseConstant;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: Shiro的配置
 * @date 2019-08-20
 */
@Configuration
public class ShiroConfiguration {


    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AnonUrlListConfiguration anonUrlListConfiguration;

    /**
     *
     * @author
     * @date 2019-09-10
     * @param securityManager 安全管理器
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置 securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 在 Shiro过滤器链上加入 JWTFilter
        LinkedHashMap<String, Filter> filters = new LinkedHashMap<>();
        filters.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filters);

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 所有请求都要经过 jwt过滤器
        filterChainDefinitionMap.put("/**", "jwt");

        //处理不需要认证的接口
        List<String> anonUrlList = anonUrlListConfiguration.getAnonUrlList();
        if(!CollectionUtils.isEmpty(anonUrlList)){
            anonUrlList.stream().forEach(item -> {
                filterChainDefinitionMap.put(item, "anon");
            });
        }
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }



    /**
      * 注册安全管理器
      * @author
      * @date 2019-09-10
      */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = new ArrayList<>();
        //添加多个Realm
        realms.add(shiroAuthorizationRealm());
        securityManager.setRealms(realms);
        return securityManager;
    }

    /**
      * 管理系统的Realm
      */
    @Bean("platformAuth")
    public ShiroAuthorizationRealm shiroAuthorizationRealm() {
        // 配置 Realm
        return new ShiroAuthorizationRealm();
    }

     /**
      * 前端APP的Realm

     @Bean("appAuth")
         public AppAuthorizationRealm appShiroRealm() {
         // 配置 Realm
         return new AppAuthorizationRealm();
     }*/


     /**
       * 开启shiro aop注解支持 使用代理方式;所以需要开启代码支持;
       * @author
       * @date 2019-09-10
       * @param securityManager 安全管理器
       * @return AuthorizationAttributeSourceAdvisor

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
    return authorizationAttributeSourceAdvisor;

    } */


    /**
      * 系统自带的Realm管理，主要针对多realm 授权
      *
      * @date 2019-09-10
      * @return ModularRealmAuthorizer

    @Bean
    public ModularRealmAuthorizer modularRealmAuthorizer() {
        //自己重写的ModularRealmAuthorizer
        ModularRealmAuthorizer modularRealmAuthorizer = new ModularRealmAuthorizer();
        return modularRealmAuthorizer;
    }  */


     /**
      * 系统自带的Realm管理，主要针对多realm 认证
      *
      * @date 2019-09-10
      * @return ModularRealmAuthorizer

     @Bean
     public ModularRealmAuthenticator modularRealmAuthenticator() {
         //自己重写的ModularRealmAuthenticator
         CustomRealmAuthenticator modularRealmAuthenticator = new CustomRealmAuthenticator();
         modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy()); //至少一个认证器通过认证
         return modularRealmAuthenticator;
     } */
}
