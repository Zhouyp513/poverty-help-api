package cn.poverty.service.config.shiro;

import cn.poverty.common.utils.spring.SpringContextHandler;
import cn.poverty.common.constants.BaseConstant;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * JWT的工具类
 * @author
 * @projectName poverty-help-api
 * @date 2019-08-20
 */
@Slf4j
public class JwtUtil {

    /**
      * 过期时间
      */
    private static final long EXPIRE_TIME = SpringContextHandler.getBean(BaseConstant.class).getAuthExpiredTime();

    /**
     * 校验token是否正确
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String userName, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("userName", userName)
                    .build();
            verifier.verify(token);
            log.info("token is valid");
            return true;
        } catch (Exception e) {
            log.info("token is invalid{}", e.getMessage());
            return false;
        }
    }

    /**
     * 从 token中拿到用户名
     * @return token中包含的用户名
     */
    public static String getUserName(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userName").asString();
        } catch (JWTDecodeException e) {
            log.error("error：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 生成 token
     * @param userName 用户名
     * @param secret   用户的密码
     * @return token
     */
    public static String sign(String userName, String secret) {
        try {
            userName = StringUtils.lowerCase(userName);
            Date plusDate = Date.from(Instant.now(Clock.systemDefaultZone()).plus(EXPIRE_TIME, ChronoUnit.MINUTES));
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim("userName", userName)
                    .withExpiresAt(plusDate)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("error：{}", e);
            return null;
        }
    }

}
