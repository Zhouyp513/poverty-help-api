package cn.poverty.common.utils.log;

import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import cn.poverty.common.utils.http.NetworkUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;

/**
 
 * @time 2018/9/29
 * @description
 */
public class MdcUtils {


    public static final String KEY_MSG_ID = "_GLOBAL_MSG_ID";
    private static final String KEY_IP = "IP";
    private static final String KEY_URL = "URL";
    private static String ip;
    private static String shotIp;

    public static void init() {
        MDC.clear();
        MDC.put(KEY_IP,ip);
    }
    public static void setIp() {
        MDC.put(KEY_IP,ip);
    }
    public static void setIp(String ip) {
        MdcUtils.ip = ip;
        MDC.put(KEY_IP,ip);
        MdcUtils.shotIp = NetworkUtils.shortIp(ip);
    }
    public static String getIp() {
        return ip;
    }
    public static void removeIp() {
        MDC.remove(KEY_IP);
    }
    public static String getShotIp() {
        return shotIp;
    }
    public static void setUrL(String url) {
        MDC.put(KEY_URL,url);
    }
    public static String getUrL() {
        return MDC.get(KEY_URL);
    }
    public static void removeUrL() {
        MDC.remove(KEY_URL);
    }
    public static void setMsgId(String id) {
        MDC.put(KEY_MSG_ID, id);
    }
    public static String getMsgId() {
        return MDC.get(KEY_MSG_ID);
    }
    public static void removeMsgId() {
        MDC.remove(KEY_MSG_ID);
    }

    /**
     * 生成一个唯一的消息id，用于日志埋点
     * @return
     */
    public static String generateId(String ip) {
        StringBuilder sb = new StringBuilder(22);

        if( null == shotIp) {
            setIp(ip);
        }

        sb.append("wt_").append(getIp());
        sb.append("_").append(SnowflakeIdWorker.uniqueStringSequence());
        return sb.toString();
    }
    /**
     * 设置MDC中msgId的值，为空则生成一个
     * @return
     */
    public static String setOrGenMsgId(String msgId,String ip) {
        if (StringUtils.isBlank(msgId)) {
            msgId = generateId(ip);
        }
        setMsgId(msgId);
        return msgId;
    }
    /**
     * 从MDC中拿到msgId，没有则生成一个
     * @return
     */
    public static String getOrGenMsgId(String ip) {
        String msgId = getMsgId();
        if (StringUtils.isBlank(msgId)) {
            msgId = generateId(ip);
            setMsgId(msgId);
        }
        return msgId;
    }


    public static void main(String[] args){
        System.out.println(MdcUtils.getOrGenMsgId("127.0.0.1"));
    }
}
