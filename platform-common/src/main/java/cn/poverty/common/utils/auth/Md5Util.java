package cn.poverty.common.utils.auth;

import cn.hutool.crypto.SecureUtil;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author
 * @packageName cn.poverty.common.utils.common
 * @Description: md5工具类
 * @date 2021-01-21
 */
public class Md5Util {

    /**
     * 对字符串进行MD5加密
     * @author
     * @date 2021/3/30
     * @param plainText 待加密的字符串
     * @return String
     */
    public static String md5Hex(String plainText) {
        return DigestUtils.md5Hex(plainText);
    }

    /**
     * 对字符串进行MD5加密
     * @author
     * @date 2021/3/30
     * @param plainText 待加密的字符串
     * @return String
     */
    public static String md5(String plainText){
        return SecureUtil.md5(plainText);
    }

}
