package cn.poverty.common.constants;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 
 * @packageName cn.poverty.common.constants
 * @Description: 权限缓存相关
 * @date 2021-04-23
 */
@Data
@Component
public class AuthConstants {

    /**
     * 已经认证了的用户基元信息缓存前缀->API平台用户
     */
    public static final String AUTHENTICATED_USER_META_TOKEN = "authenticated.user.meta.token.";

    /**
     * 登录用户的角色缓存前缀
     */
    public static final String AUTHENTICATED_USER_ROLE = "authenticated.user.role.";

    /**
     * user权限缓存前缀
     */
    public static final String AUTHENTICATED_USER_PERMISSION = "authenticated.user.permission.";

    /**
     * user个性化配置前缀
     */
    public static final String AUTHENTICATED_USER_CONFIG = "authenticated.user.config.";

    /**
     * token缓存前缀
     */
    public static final String AUTHENTICATED_TOKEN = "authenticated.token.";

    /**
     * 存储在线用户的信息(变量是用户ID)
     */
    public static final String AUTHENTICATED_ACTIVE_USER = "authenticated.active_user.";

    /**
     * 存储在线用户的总数
     */
    public static final String AUTHENTICATED_ACTIVE_USERS_COUNT = "authenticated.active_users_count";

    /**
     * 存储在线用户的 zSet前缀
     */
    public static final String AUTHENTICATED_ACTIVE_USERS = "authenticated.active_users";

    /**
     * token的名称
     */
    public static final String TOKEN = "Authentication";

}
