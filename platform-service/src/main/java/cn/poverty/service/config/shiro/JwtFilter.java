package cn.poverty.service.config.shiro;


import cn.poverty.common.enums.ErrorCode;
import cn.poverty.common.utils.auth.AuthEncrypt;
import cn.poverty.common.utils.spring.SpringContextHandler;
import cn.poverty.interaction.resp.base.ApiResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * JWT的过滤器
 * 
 * @date 2019-08-20
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
      * token的名称
      */
    private static final String TOKEN = "Authentication";

    /**
     * //系统接口访问的类型
     */
    private static final String API_REQUEST_TYPE = "apiRequestType";

    /**
     * 授权匹配器
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
      * 判断是否允许进入的方法
      * 
      * @date 2019-08-20
      * @param request ServletRequest
      * @param response ServletResponse
      * @param mappedValue 请求映射的对象
      * @return boolean
      */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        AnonUrlListConfiguration anonUrlListConfiguration = SpringContextHandler.getBean(AnonUrlListConfiguration.class);
        List<String> anonUrlList = anonUrlListConfiguration.getAnonUrlList();
        log.info(">>>>>>>>>>>>>>匿名访问的URL:{}<<<<<<<<<<<<<<<",JSON.toJSONString(anonUrlList));
        boolean match = false;
        for (String u : anonUrlList) {
            log.info(">>>>>>>>>>>>>>请求源地址{}<<<<<<<<<<<<<<<",httpServletRequest.getRequestURI());
            if (pathMatcher.match(u, httpServletRequest.getRequestURI())){
                match = true;
            }
        }
        log.info(">>>>>>>>>>>>>>是否匹配<<<<<<<<<<<<<<< "+match);
        if (match) {
            return true;
        }
        //试图登陆的方法
        if (isLoginAttempt(request, response)) {
            return executeLogin(request, response);
        }
        return false;
    }

    /**
     * 处理是否试图登陆
     * 
     * @date 2019-09-09
     * @param request 请求
     * @param response 返回
     * @return boolean
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(TOKEN);
        //如果Headers里有 Authorization的值 那么就为登录
        return token != null;
    }

    /**
      * 执行登陆
      * 
      * @date 2021/3/30
      * @param request  ServletRequest
      * @param response ServletResponse
      * @return boolean
      */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>请求路径<<<<<<<<<<<<<<<<<<<<<<<<"+((HttpServletRequest) request).getRequestURI());
        String token = httpServletRequest.getHeader(TOKEN);
        String apiRequestType = httpServletRequest.getHeader(JwtFilter.API_REQUEST_TYPE);
        //让前端决定访问类型
        JwtToken jwToken = new JwtToken(AuthEncrypt.decryptToken(token));
        jwToken.setApiAccessType(apiRequestType);
        try {
            getSubject(request, response).login(jwToken);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
      * 对跨域提供支持
      * 
      * @date 2021/3/30
      * @param request  ServletRequest
      * @param response ServletResponse
      * @return boolean
      */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个 option请求，这里我们给 option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
      *
      * 
      * @date 2019-08-21
      * @param request ServletRequest
      * @param response ServletResponse
      * @return boolean
      */
    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>请求路径: {} <<<<<<<<<<<<<<<<<<<<<<<<",
                ((HttpServletRequest) request).getRequestURI());
        log.debug("Authentication required: sending 401 Authentication challenge response.");
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setCharacterEncoding("utf-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        String message = "未认证，请在前端系统进行认证";
        try (PrintWriter out = httpResponse.getWriter()) {
            ApiResponse resp = new ApiResponse();
            resp.setCode(ErrorCode.AUTH__ERROR.getCode());
            resp.setMessage(ErrorCode.AUTH__ERROR.getMessage());
            resp.setData("");
            //String responseJson = "{\"message\":\"" + message + "\"}";
            out.print(JSON.toJSONString(resp));
        } catch (IOException e) {
            log.error("sendChallenge error：", e);
        }
        return false;
    }

    public static void main(String[] args) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean match = pathMatcher.match("/actuator/**", "/actuator/metrics/jvm.threads.live");
        System.out.println(match);
    }
}

