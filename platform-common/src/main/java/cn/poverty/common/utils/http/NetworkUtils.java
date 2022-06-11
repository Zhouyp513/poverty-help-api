package cn.poverty.common.utils.http;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 系统网络工具类
 
 * @time 2018/9/29
 */
@Slf4j
public class NetworkUtils {

    /**
     * 短IP格式
     */
    public static final String SHORT_IP_FORMAT = "%s.%s";

    /**
     * 短IP长度
     */
    public static final String SHORT_IP_LENGTH = "4";

    /**
     * unknown
     */
    public static final String UN_KNOWN = "unknown";

    /**
     * LOCAL_IP 本地IP
     */
    public static final String LOCAL_IP = "127.0.0.1";


    /**
     * IPv4的长度:15
     */
    public static final Integer IP_V4_LENGTH = 15;

    /**
     * 多IP处理，可以得到最终ip
     *
     * @return
     */
    public static IpAddress getIp() {
        // 本地IP，如果没有配置外网IP则返回它
        List<String> localIps = new ArrayList<>();
        // 外网IP
        List<String> netIps = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        // 内网IP
                        if (ip.isSiteLocalAddress()) {
                            localIps.add(ip.getHostAddress());
                        } else {//外网IP
                            netIps.add(ip.getHostAddress());
                        }
                    }
                }

            }
        } catch (SocketException e) {
            log.error("", e);
        }

        return new IpAddress(localIps, netIps);
    }

    /**
     * 根据IP拿到短IP地址
     * 
     * @date 2021/3/29
     * @param ip 短IP地址
     * @return String
     */
    public static String shortIp(String ip) {
        String[] sip = ip.split("\\.");
        Integer sipLength = 4;
        if (sip.length >= sipLength) {
            return String.format(SHORT_IP_FORMAT, sip[2], sip[3]);
        }
        return ip;
    }


    public static class IpAddress {
        final static String DEFAULT_IP = "127.0.0.1";
        public final List<String> localIPs;
        public final List<String> netIPs;

        public IpAddress(List<String> localIps, List<String> netIps) {
            this.localIPs = localIps;
            this.netIPs = netIps;
        }

        public String getI() {
            if (netIPs != null && netIPs.size() > 0) {
                return netIPs.get(0);
            }
            if (localIPs == null || localIPs.size() == 0) {
                return DEFAULT_IP;
            }
            return localIPs.get(0);
        }

        @Override
        public String toString() {
            return "IpAddress{" +
                    "localIPs=" + localIPs +
                    ", netIPs=" + netIPs +
                    '}';
        }
    }

    /**
     * 拿到客户端的ip地址
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request){

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCAL_IP.equals(ip)) {
                // 根据网卡取本机配置的IP
                try {
                    ip = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        // "***.***.***.***".length()=15
        if (ip != null && ip.length() > IP_V4_LENGTH) {
            int i = ip.indexOf(",");
            if (i > 0) {
                ip = ip.substring(0,i);
            }
        }
        return ip;
    }
}
