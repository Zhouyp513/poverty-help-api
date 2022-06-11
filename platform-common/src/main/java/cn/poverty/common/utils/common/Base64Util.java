package cn.poverty.common.utils.common;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 * @author lha
 * base64加密解密
 */
public class Base64Util {

	/**
	 * 需要加密的额外参数前缀
	 */
	private static final String BASE_START="lancaizhu";


	/**
	 * 需要加密的后缀
	 */
	private static final String BASE_END="com";


	/**
	 *
	 * @param value 需要加密的字符串
	 * @return 组装成新的需要加密的字符串
	 */
	public static String createBaseStr(String value){
		value=BASE_START+value+BASE_END;
		return value;
	}

	/**
	 * 将传递的参数进行base64加密
	 * @param value 需要加密的参数
	 * @return 返回base64加密后的参数
	 */
	public static String encodeBase64(String value){
		value=createBaseStr(value);
		byte[] bt=value.getBytes();
		Base64 base64=new Base64();
		bt=base64.encode(bt);
		String str=new String(bt);
		return str;

	}

	/**
	 * @param value 需要解密的参数
	 * @return 返回base64解密参数
	 */
	public static String decodeBase64(String value){

		value=new String(Base64.decodeBase64(value));
		value=value.substring(BASE_START.length());
		value=value.substring(0, value.length()-BASE_END.length());
		return value;
	}

}
