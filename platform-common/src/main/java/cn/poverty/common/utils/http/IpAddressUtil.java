package cn.poverty.common.utils.http;

import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.interaction.AddressFromIpVo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 通过IP拿到地址的工具
 * @date 2019-08-21
 */
@Slf4j
public class IpAddressUtil {


    public static void main(String[] args) {
        String url = "http://ip.360.cn/IPQuery/ipquery";
        Map<String,String> paramMap = new HashMap<>(16);
        paramMap.putIfAbsent("ip","118.112.72.171");
        String request = HttpTookit.postRequest(url, paramMap);
        System.out.println(request);
        System.out.println(queryAddressByIpFrom360("118.112.72.171",url));
    }



    /**
      *
      * 从360的IP分享计划接口拿到详细地理位置
      *
      * @date 2019-08-21
      * @param ip IP地址
      * @param url 请求url地址
      * @return String
      */
    public static String queryAddressByIpFrom360(String ip,String url){
        Map<String,String> paramMap = new HashMap<>(16);
        paramMap.putIfAbsent("ip",ip);

        String data = HttpTookit.postRequest(url, paramMap);

        if(CheckParam.isNull(data)) {
            return null;
        }

        AddressFromIpVo addressFromIpVo = JSON.parseObject(data, AddressFromIpVo.class);

        return addressFromIpVo.getData().trim();
    }

}
