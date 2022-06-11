package cn.poverty.common.utils.auth;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**

 * @time 2017/11/16
 * 系统加密工具类
 */
public class EncryptUtils {

    /**
     * MD5输入字符串
     * @author
     * @date 2019-09-23
     * @param input 输入字符串
     * @return String
     */
    public static String md5String(String input) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "check jdk";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = input.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++){
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 通过盐进行MD5
     * @author
     * @date 2019-09-23
     * @param password 密码
     * @param salt 盐
     * @return String
     */
    public static String encrypt(String password, String salt) {
        return md5Hex(password + salt);
    }


    /**
     * 生成盐
     * @author
     * @date 2019-09-23
     * @return String
     */
    public static String generateSalt(){
        Integer maxLength = 16;
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < maxLength) {
            for (int i = 0; i < maxLength - len; i++) {
                sb.append("0");
            }
        }
        return sb.toString();
    }



    /**
     * 拿到十六进制字符串形式的MD5摘要
     * @author
     * @date 2019-09-23
     * @param  src 字符串原值
     * @return String
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 校验加盐后是否和原文一致
     * @author Singer
     * @time 2017/11/16
     * @param password
     * @param md5
     * @return boolean
     */
    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];

        Integer maxRecycle = 48;
        Integer maxLength = 48;

        for (int i = 0; i < maxRecycle; i += maxLength) {
            cs1[i / maxLength * 2] = md5.charAt(i);
            cs1[i / maxLength * 2 + 1] = md5.charAt(i + 2);
            cs2[i / maxLength] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }


    /**
     * 验证密码
     * @param password 加盐后的密码
     * @param originalPassword 需要被验证的密码和盐生成的密码
     * @param authSalt 密码盐
     * @return boolean
     */
    public static boolean verifyPassword(String password,String originalPassword,String authSalt){
        return password.equals(encrypt(originalPassword,authSalt));
    }

    public static void main(String[] args) {
        String  password = "123456";
        String salt = "dcb73f9efe47762905f03d5943a02493";
        String generate = encrypt(password, salt);
        System.out.println(generate);
        System.out.println(verifyPassword(generate,password,salt));
    }

}
