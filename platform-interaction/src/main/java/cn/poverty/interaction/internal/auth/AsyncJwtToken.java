package cn.poverty.interaction.internal.auth;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Singer create by singer email:singer-coder@qq.com
 * @projectName common-platform-api
 * @Description: 异步的JWT的token
 * @date 2019-08-20
 */
@Data
public class AsyncJwtToken implements AuthenticationToken {


    private static final long serialVersionUID = -3642278557125793450L;

    /**
     * 系统的token
     */
    private String token;

    /**
     * 过期时间
     */
    private String exipreAt;

    /**
     * 系统API访问TYPE
     */
    private String apiAccessType;

    public AsyncJwtToken(String token) {
        this.token = token;
    }

    public AsyncJwtToken(String token, String exipreAt, String apiAccessType) {
        this.token = token;
        this.exipreAt = exipreAt;
        this.apiAccessType = apiAccessType;
    }

    public AsyncJwtToken(String token, String exipreAt) {
        this.token = token;
        this.exipreAt = exipreAt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
