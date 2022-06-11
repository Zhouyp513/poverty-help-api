package cn.poverty.common.utils.auth;

import cn.poverty.common.constants.AuthConstants;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.Security;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 权限加密工具
 * @date 2019-08-20
 */
@Slf4j
public class AuthEncrypt {

    /**
     * 设置默认密匙
     */
    private static String strDefaultKey = "defaultKey";

    /**
     * 加密
     */
    private Cipher encryptCipher = null;

    /**
     * 解密
     */
    private Cipher decryptCipher = null;

    private static final String ALGORITH_NAME = "md5";

    private static final int HASH_ITERATIONS = 2;


    /**
     * 对密码进行加密
     *
     * @date 2019-08-21
     * @param  password 密码
     * @return
     */
    public static String encryptPassword(String password) {
        return EncryptUtils.md5String(password);
    }



    /**
     * token 加密
     *
     * @param token token
     * @return 加密后的 token
     */
    public static String encryptToken(String token) {
        try {
            AuthEncrypt encryptUtil = new AuthEncrypt(AuthConstants.AUTHENTICATED_TOKEN);
            return encryptUtil.encrypt(token);
        } catch (Exception e) {
            log.info("token加密失败：", e);
            return null;
        }
    }

    /**
     * token 解密
     * @param encryptToken 加密后的 token
     * @return 解密后的 token
     */
    public static String decryptToken(String encryptToken) {
        try {
            AuthEncrypt encryptUtil = new AuthEncrypt(AuthConstants.AUTHENTICATED_TOKEN);
            return encryptUtil.decrypt(encryptToken);
        } catch (Exception e) {
            log.info("token解密失败：", e);
            return null;
        }
    }

    private static String byteArr2HexStr(byte[] arrB) {
        int iLen = arrB.length;
        StringBuilder sb = new StringBuilder(iLen * 2);
        for (byte anArrB : arrB) {
            int intTmp = anArrB;
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    private static byte[] hexStrToByteArr(String strIn) {

        Integer hash2 = 2;

        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        byte[] arrOut = new byte[iLen / hash2];
        for (int i = 0; i < iLen; i = i + hash2) {
            String strTmp = new String(arrB, i, hash2);
            arrOut[i / hash2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    public AuthEncrypt() throws Exception {
        this(strDefaultKey);
    }

    AuthEncrypt(String strKey) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());

        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    private byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    private byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    String decrypt(String strIn) {
        try {
            return new String(decrypt(hexStrToByteArr(strIn)));
        } catch (Exception e) {
            return "";
        }
    }

    private Key getKey(byte[] arrBtmp) {
        byte[] arrB = new byte[8];
        for (int i = 0; i < arrBtmp.length && i < arrB.length; i++) {
            arrB[i] = arrBtmp[i];
        }
        return new javax.crypto.spec.SecretKeySpec(arrB, "DES");
    }
}
