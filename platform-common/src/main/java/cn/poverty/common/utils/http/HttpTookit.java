package cn.poverty.common.utils.http;

import cn.poverty.common.utils.common.CheckParam;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 
 * @packageName cn.poverty.common.utils.http
 * @Description: HTTP工具类
 * @date 2021-01-21
 */
@Slf4j
public class HttpTookit {

    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 800;
    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 400;
    /**
     * 拿到连接的最大等待时间
     */
    public final static int WAIT_TIMEOUT = 1000;
    /**
     * 连接超时时间
     */
    public final static int CONNECT_TIMEOUT = 5000;
    /**
     * 读取超时时间
     */
    public final static int READ_TIMEOUT = 1000000;

    /**
     * 400错误
     */
    public final static int ERROR_CODE_400 = 400;

    /**
     * 竞争锁资源
     */
    private static final byte[] LOCK = new byte[0];

    private static volatile CloseableHttpClient defaultHttpClient;

    public static CloseableHttpClient getHttpClient() {
        if (defaultHttpClient == null) {
            synchronized (LOCK) {
                if (defaultHttpClient == null) {
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                            .setConnectionRequestTimeout(WAIT_TIMEOUT)
                            .setConnectTimeout(CONNECT_TIMEOUT)
                            .setSocketTimeout(READ_TIMEOUT).build();
                    SSLConnectionSocketFactory ssf = null;
                    try {
                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init(null, new X509TrustManager[] {new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                        }}, null);
                        ssf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
                    } catch (NoSuchAlgorithmException | KeyManagementException e) {

                    }
                    defaultHttpClient = HttpClientBuilder.create()
                            .setMaxConnTotal(MAX_TOTAL_CONNECTIONS)
                            .setMaxConnPerRoute(MAX_ROUTE_CONNECTIONS)
                            .setSSLSocketFactory(ssf)
                            .setDefaultRequestConfig(requestConfig).build();
                }
            }
        }
        return defaultHttpClient;
    }

    public static void main(String[] args) {



    }

    /**
     * 发送POST请求 请求数据放在请求body里面
     
     * @date 2018/12/4 9:14
     * @param url 请求URL地址
     * @param urlParamMap 请求URL拼接参数
     * @param paramMap 请求body参数
     * @param headerParams 请求header
     * @return String
     */
    public static String postBodyRequest(String url,
                                         Map<String, Object> urlParamMap,
                                         Map<String, Object> paramMap,
                                         Map<String, String> headerParams) throws IOException {
        if(!CheckParam.isNull(urlParamMap) && !urlParamMap.isEmpty()){
            url = getParameters(url, urlParamMap, Consts.UTF_8.name());
        }

        HttpPost post = new HttpPost(url);
        initHeader(post, headerParams);

        StringEntity stringEntity = new StringEntity(JSON.toJSONString(paramMap),ContentType.APPLICATION_JSON.getMimeType());
        post.setEntity(stringEntity);
        return getResult(post);
    }


    /**
     * 发送POST请求 请求数据放在请求body里面
     
     * @date 2018/12/4 9:14
     * @param url 请求URL地址
     * @param urlParamMap 请求URL拼接参数
     * @param paramMap 请求body参数
     * @param headerParams 请求header
     * @return InputStream
     */
    public static byte[] postBodyRequestBuffer(String url,
                                               Map<String, Object> urlParamMap,
                                               Map<String, Object> paramMap,
                                               Map<String, String> headerParams) throws IOException {
        if(!CheckParam.isNull(urlParamMap) && !urlParamMap.isEmpty()){
            url = getParameters(url, urlParamMap, Consts.UTF_8.name());
        }

        HttpPost post = new HttpPost(url);
        initHeader(post, headerParams);

        StringEntity stringEntity = new StringEntity(JSON.toJSONString(paramMap),
                ContentType.APPLICATION_JSON.getMimeType());
        post.setEntity(stringEntity);
        return getResultBuffer(post);
    }

    public static String postRequest(String uri, Map<String, String> rawParams, Map<String, String> headerParams) {
        HttpPost post = new HttpPost(uri);
        initHeader(post, headerParams);
        List<NameValuePair> params = new ArrayList<>();
        if (rawParams != null) {
            params.addAll(rawParams.keySet().stream().map(key -> new BasicNameValuePair(key, rawParams.get(key))).collect(Collectors.toList()));
        }
        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        return getResult(post);
    }


    public static String postRequest(String uri, Map<String, String> rawParams) {
        return postRequest(uri, rawParams, null);
    }

    public static String postRequest(String uri) {
        HttpPost post = new HttpPost(uri);
        return getResult(post);
    }

    public static String postRequest(String uri, String json, Map<String, String> headerParams) {
        HttpPost post = new HttpPost(uri);
        initHeader(post, headerParams);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        post.setEntity(entity);
        return getResult(post);
    }

    public static String postRequest(String uri, String json) {
        return postRequest(uri, json, null);
    }


    /**
      * 
      * @date 2021/5/18
      * @param uri 请求地址
      * @param headerParams header参数
      * @return String
      */
    public static String getRequest(String uri, Map<String, String> headerParams) {
        HttpGet get = new HttpGet(uri);
        initHeader(get, headerParams);
        return getResult(get);
    }

    public static String getRequest(String uri) {
        HttpGet get = new HttpGet(uri);
        return getResult(get);
    }


    /**
     * 拿到请求的结果 拿到的是
     * 
     * @date 2021/2/27
     * @param request 请求对象
     * @return String
     */
    public static byte[] getResultBuffer(HttpUriRequest request) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpEntity httpEntity = null;
        try(CloseableHttpResponse httpResponse = httpClient.execute(request)){
            try {
                httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                byte[] bytes = IoUtil.readBytes(inputStream, true);
                return bytes;
            } finally {
                if(httpEntity != null) {
                    EntityUtils.consume(httpEntity);
                }
            }
        } catch (IOException e) {
            log.error("请求失败,error:{}",e);
        }
        return null;
    }

    /**
     * 拿到请求的结果
     * 
     * @date 2021/2/27
     * @param request 请求对象
     * @return String
     */
    public static String getResult(HttpUriRequest request) {
        CloseableHttpClient httpClient = getHttpClient();
        String result = "";
        HttpEntity httpEntity = null;
        try(CloseableHttpResponse httpResponse = httpClient.execute(request)){
            try {
                httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                String line = null;
                while ((line=reader.readLine())!=null){
                    result += line;
                }
                return result;
            } finally {
                if(httpEntity != null) {
                    EntityUtils.consume(httpEntity);
                }
            }

        } catch (IOException e) {
            log.error("请求失败,error:{}",e);
        }
        return null;
    }


    /**
     * 从请求返回结果里面解析出数据
     * 
     * @date 2021/2/4
     * @param httpResponse 请求返回结果
     * @return String
     */
    public static String getResult(CloseableHttpResponse httpResponse) {
        String result = "";
        HttpEntity httpEntity = null;
        try {
            httpEntity = httpResponse.getEntity();
            InputStream in = null;
            in = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = null;
            while ((line=reader.readLine())!=null){
                result += line;
            }

            return result;
        }catch (Exception e){
            log.info(">>>>>>>>>>>>>>>解析请求结果出现异常: {} , {} <<<<<<<<<<<<<<",e.getMessage(),e);
        }finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                log.info(">>>>>>>>>>>>>>>解析请求结果出现异常: {} , {} <<<<<<<<<<<<<<",e.getMessage(),e);
            }
        }
        return null;
    }

    /**
     * HTTP Get 拿到内容
     * @param url  请求的url地址 ?之前的地址
     * @param params	请求的参数
     * @param charset	编码格式
     * @param proxyIp	代理IP
     * @param proxyHost	代理端口
     * @return	页面内容
     */
    public static String getRequestByProxy(
            String url,
            Map<String,Object> params,
            String charset,
            String proxyIp,
            Integer proxyHost){
        CloseableHttpClient httpClient = getHttpClient();
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            url = getParameters(url, params, charset);
            HttpGet httpGet = new HttpGet(url);

            // 依次是代理地址，代理端口号，协议类型
            HttpHost proxy = new HttpHost(proxyIp, proxyHost, "http");
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(15000)
                    .setConnectionRequestTimeout(15000)
                    .setConnectionRequestTimeout(15000).setProxy(proxy).build();

            httpGet.setConfig(config);

            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= ERROR_CODE_400) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, Charset.defaultCharset());
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            log.error("请求异常,error :{}",e);
        }
        return null;
    }

    /**
     * HTTP Get 拿到内容
     * @param url  请求的url地址 ?之前的地址
     * @param params	请求的参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static String getRequest(String url,Map<String,Object> params,String charset){
        CloseableHttpClient httpClient = getHttpClient();
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            url = getParameters(url, params, charset);
            HttpGet httpGet = new HttpGet(url);

            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= ERROR_CODE_400) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, Charset.defaultCharset());
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            log.error("请求异常,error :{}",e);
        }
        return null;
    }

    public static String getParameters(String url, Map<String, Object> params, String charset) throws IOException {
        if(params != null && !params.isEmpty()){
            List<NameValuePair> pairs = new ArrayList<>(params.size());
            for(Map.Entry<String,Object> entry : params.entrySet()){
                String value = String.valueOf(entry.getValue());
                if(value != null){
                    pairs.add(new BasicNameValuePair(entry.getKey(),value));
                }
            }
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
        }
        return url;
    }

    public static String getParameters(String url, Map<String, Object> params, Map<String, Object> pathParams,String charset) throws IOException {
        if(params != null && !params.isEmpty()){
            List<NameValuePair> pairs = new ArrayList<>(params.size());
            for(Map.Entry<String,Object> entry : params.entrySet()){
                String value = String.valueOf(entry.getValue());
                if(value != null){
                    pairs.add(new BasicNameValuePair(entry.getKey(),value));
                }
            }

            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
        }
        return url;
    }

    /**
     * 初始化消息头
     *
     * @param request GET/POST...
     */
    public static void initHeader(HttpUriRequest request, Map<String, String> headerParams) {
        if (headerParams != null) {
            for (String key: headerParams.keySet()) {
                request.addHeader(key, headerParams.get(key));
            }
        }
    }

    public static String getRequest(String uri,String contentEnCoding) {
        HttpGet get = new HttpGet(uri);
        return getResult(get,contentEnCoding);
    }
    private static String getResult(HttpUriRequest request, String contentEncoding) {
        CloseableHttpClient httpClient = getHttpClient();
        try (CloseableHttpResponse httpResponse = httpClient.execute(request)) {
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity,contentEncoding);
        } catch (IOException e) {
            log.error("请求失败,error:{}",e);
        }
        return null;
    }


    public static String putRequest(String uri, Map<String, Object> rawParams) {
        HttpPut put = new HttpPut(uri);
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(rawParams);
        StringEntity entity = new StringEntity(jsonObject.toString(),"UTF-8");
        entity.setContentType("application/json");
        put.setEntity(entity);
        return getResult(put);
    }

}
