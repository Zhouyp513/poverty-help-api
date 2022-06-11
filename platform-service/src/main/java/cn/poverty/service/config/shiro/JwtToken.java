package cn.poverty.service.config.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: JWT的token
 * @date 2019-08-20
 */
@Data
public class JwtToken implements AuthenticationToken {


    private static final long serialVersionUID = 1309705344533517254L;

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

    public JwtToken(String token) {
        this.token = token;
    }

    public JwtToken(String token, String exipreAt, String apiAccessType) {
        this.token = token;
        this.exipreAt = exipreAt;
        this.apiAccessType = apiAccessType;
    }

    public JwtToken(String token, String exipreAt) {
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
