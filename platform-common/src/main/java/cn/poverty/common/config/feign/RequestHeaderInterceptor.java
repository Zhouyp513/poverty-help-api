/*
package poverty.common.config.feign;

import poverty.common.utils.CheckParam;
import poverty.common.utils.Md5Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;


*/
/**
 * Header拦截器
 
 * @time 2018/10/10
 * @description
 *//*

@Slf4j
public class RequestHeaderInterceptor implements RequestInterceptor {

    private String key;

    public RequestHeaderInterceptor(String key) {
        this.key = key;
    }

    public RequestHeaderInterceptor(){}

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, String> headers = getHeaders();

        //为请求设置原本的Header
        headers.forEach((k,v) -> {
            requestTemplate.header(k, v);
        });

        SortedMap<String, String> params = new TreeMap<>();
        String bodyStr = null;
        try {
            if (requestTemplate.body() != null && requestTemplate.body().length > 0) {
                bodyStr = new String(requestTemplate.body(),"UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("check body params error...");
        }
        if (!CheckParam.isNull(bodyStr) && bodyStr.length() > 0) {
            params = JSONObject.parseObject(bodyStr, SortedMap.class);
            bodyStr = JSON.toJSONString(signParams(params));
            requestTemplate.body(bodyStr);
        }else {
            Map<String, Collection<String>> queries = requestTemplate.queries();
            Iterator<String> iter = queries.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                ArrayList<String> vals = (ArrayList<String>)queries.get(key);
                String val = vals.get(0);
                params.put(key, val);
            }
            params = signParams(params);
            Collection<String> col = new ArrayList<>();
            col.add(params.get("sign"));
            Map<String, Collection<String>> newQueries = new HashMap<>();
            newQueries.putAll(queries);
            newQueries.put("sign", col);
            requestTemplate.queries(newQueries);
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private SortedMap<String,String> signParams(SortedMap<String,String> params) {

        Iterator<String> iter = params.keySet().iterator();
        String signStr = "";
        while (iter.hasNext()) {
            String key = iter.next();
            signStr += key + "=" + String.valueOf(params.get(key)) + "&";
        }
        signStr += "key=" + key;
        params.put("sign", Md5Util.md5(signStr));
        return params;
    }

    */
/**
     *  返回所有header 中的数据
     *   Return
     *//*

    private Map<String, String> getHeaders(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(null == requestAttributes){
            return Maps.newLinkedHashMap();
        }
        HttpServletRequest request =  ((ServletRequestAttributes) requestAttributes).getRequest();
        //为request中的数据加签名
        Map<String, String> map = Maps.newLinkedHashMap();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            if("content-type".equals(key)) {
                continue;
            }
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

}
*/
