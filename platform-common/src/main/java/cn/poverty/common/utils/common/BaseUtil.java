package cn.poverty.common.utils.common;

import cn.hutool.core.codec.Base64;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 
 * @packageName cn.poverty.common.utils.common
 * @Description: 基础的工具类
 * @date 2021-01-21
 */
@Slf4j
public class BaseUtil {

	private static SimpleDateFormat shortformat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat commonFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");


	/**
	 * 字符串类型
	 */
	private static String STRING_CLASS = "class java.lang.String";

	/**
	 * 整型类型
	 */
	private static String INTEGER_CLASS = "class java.lang.Integer";

	/**
	 * Double类型
	 */
	private static String DOUBLE_CLASS = "class java.lang.Double";

	/**
	 * Long类型
	 */
	private static String LONG_CLASS = "class java.lang.Long";

	/**
	 * BigDecimal类型
	 */
	private static String BIGDECIMAL_CLASS = "class java.math.BigDecimal";

	/**
	 * LocalDateTime类型
	 */
	private static String LOCALDATETIME_CLASS = "class java.time.LocalDateTime";

	/**
	 * Boolean类型
	 */
	private static String BOOLEAN_CLASS = "class java.lang.Boolean";


	private static final String UNKNOWN = "unknown";

	/**
	 * 计算两个经纬度左边之间的距离
	 * 
	 * @date 2021/8/4
	 * @param source 源地址
	 * @param source 目标地址
	 * @return java.lang.Double
	 */
	public static Double distanceMeter(GlobalCoordinates source, GlobalCoordinates target){
		//创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
		GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target);
		Double ellipsoidalDistance = geoCurve.getEllipsoidalDistance();
		return ellipsoidalDistance;
	}

	/**
	 * 设置实体类字段的默认值
	 * 
	 * @date 2021/1/26
	 * @param obj 需要被设置默认字段的实体参数
	 */
	public static void setFieldValueNotNull(Object obj) throws Exception{
		Arrays.stream(obj.getClass().getDeclaredFields()).forEach(field -> {
			field.setAccessible(true);
			try {
				if (field.get(obj) == null){
					if (STRING_CLASS.equals(field.getGenericType().toString())) {
						field.set(obj, "");
					}else if (INTEGER_CLASS.equals(field.getGenericType().toString())) {
						field.set(obj, 0);
					}else if (DOUBLE_CLASS.equals(field.getGenericType().toString())) {
						field.set(obj, 0.0);
					}else if (LONG_CLASS.equals(field.getGenericType().toString())) {
						field.set(obj, 0L);
					}else if (BIGDECIMAL_CLASS.equals(field.getGenericType().toString())) {
						field.set(obj, BigDecimal.ZERO);
					}else if (LOCALDATETIME_CLASS.equals(field.getGenericType().toString())) {
						field.set(obj, LocalDateTime.now(Clock.systemDefaultZone().getZone()));
					}else if (BOOLEAN_CLASS.equals(field.getGenericType().toString())) {
						field.set(obj, Boolean.TRUE);
					}
				}
			} catch (IllegalAccessException e) {
				log.error("设置设置实体类字段的默认值出现异常:{},{}",e.getMessage(),e);
			}
		});
	}

	/**
	 * @description: 读取classpath下面的文件的内容
	 
	 * @date 2021/1/28
	 * @param resource
	 * @return String
	 */
	public static String readClassPathContent(ClassPathResource resource) {
		try {
			if(!resource.exists()) {
				return null;
			}else{
				String content = IOUtils.toString(resource.getInputStream(),"utf-8");
				log.info(">>>>>>>>>>>>>>读取配置文件的内容<<<<<<<<<<<< : {} ", content);
				if (StringUtils.isBlank(content)) {
					log.error(">>>>>>>>>>>>>,读取JSON文件内容为空<<<<<<<<<<<<");
				}
				return content;
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(">>>>>>>>>>>>>>>>>>>读取配置文件出现异常<<<<<<<<<<<<<<<<<<: "+e.getMessage(),e);
			return null;
		}
	}

	/**
	 * SHA256withRSA签名
	 * 
	 * @date 2021/2/4
	 * @param content
	 * @param charset
	 * @return String
	 */
	public static String signByPkcs8Sha256WithRsa(String content, String charset){
		ClassPathResource apiClientPrivateKeyResource = new ClassPathResource("wxV3PayCert/apiclient_key.pem");
		try {
			String privateKey = BaseUtil.readFileContent(apiClientPrivateKeyResource.getFile());
			privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "")
					.replace("-----END PRIVATE KEY-----", "")
					.replaceAll("\\s+", "");

			PKCS8EncodedKeySpec priPkcs8 = new PKCS8EncodedKeySpec(cn.hutool.core.codec.Base64.decode(privateKey));
			PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(priPkcs8);

			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(priKey);
			signature.update(content.getBytes(charset));
			return cn.hutool.core.codec.Base64.encode(signature.sign());
		} catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>>>>>SHA256withRSA签名失败 : {} , {} <<<<<<<<<<<<<<<<<<",e.getMessage(),e);
			//签名失败
			return null;
		}
	}

	/**
	 * SHA256withRSA签名
	 * 
	 * @date 2021/2/4
	 * @param content
	 * @param charset
	 * @return String
	 */
	public static String signBySha256WithRsa(String content, String charset){
		ClassPathResource apiClientPrivateKeyResource = new ClassPathResource("wxV3PayCert/apiclient_key.pem");
		try {
			String privateKey = BaseUtil.readFileContent(apiClientPrivateKeyResource.getFile());
			privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "")
					.replace("-----END PRIVATE KEY-----", "")
					.replaceAll("\\s+", "");
			// 加载商户私钥（privateKey：私钥字符串）
			PrivateKey merchantPrivateKey = PemUtil
					.loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes(charset)));

			byte[] needSingBytes = content.getBytes(charset);
			Signature sign = Signature.getInstance("SHA256withRSA");
			sign.initSign(merchantPrivateKey);
			sign.update(needSingBytes);
			String signResult = Base64.encode(sign.sign());
			return signResult;
		} catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>>>>>SHA256withRSA签名失败 : {} , {} <<<<<<<<<<<<<<<<<<",e.getMessage(),e);
			//签名失败
			return null;
		}
	}

	/**
	 * 读取classpath下面的文件的内容
	 * 
	 * @date 2021/1/28
	 * @param file 文件内容
	 * @return String
	 */
	public static String readFileContent(File file) {
		BufferedReader reader = null;
		StringBuffer sbf = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempStr;
			while ((tempStr = reader.readLine()) != null) {
				sbf.append(tempStr);
			}
			reader.close();
			return sbf.toString();
		} catch (IOException e) {
			log.error(">>>>>>>>>>>>>>读取配置文件失败<<<<<<<<<<<< : {} , {} ", e.getMessage(),e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.error(">>>>>>>>>>>>>>读取配置文件失败<<<<<<<<<<<< : {} , {} ", e.getMessage(),e);
				}
			}
		}
		return sbf.toString();
	}

	/**
	 * 转换为驼峰
	 * @param underscoreName
	 * @return
	 */
	public static String camelCaseName(String underscoreName) {
		StringBuilder result = new StringBuilder();
		if (underscoreName != null && underscoreName.length() > 0) {
			boolean flag = false;
			for (int i = 0; i < underscoreName.length(); i++) {
				char ch = underscoreName.charAt(i);
				if ("_".charAt(0) == ch) {
					flag = true;
				} else {
					if (flag) {
						result.append(Character.toUpperCase(ch));
						flag = false;
					} else {
						result.append(ch);
					}
				}
			}
		}
		return result.toString();
	}

	/**
	 * 拿到 IP地址
	 * 使用 Nginx等反向代理软件， 则不能通过 request.getRemoteAddr()拿到 IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
	 * X-Forwarded-For中第一个非 unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddrByHttpServletRequest(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	/**
	 * 数值型字符串拿到整数部分
	 *
	 * @param str
	 * @return
	 * @author 谭军
	 */
	public static final Long getLong(String str) {
		if (CheckParam.isInteger(str)) {
			return Long.parseLong(str);
		} else if (CheckParam.isNum(str)) {
			int index = str.indexOf(".");
			String integer = str.substring(0, index);
			return Long.parseLong(integer);
		}
		return null;
	}

	/**
	 * 数值型字符串拿到整数部分
	 *
	 * @param str
	 * @return
	 * @author 谭军
	 */
	public static final Integer getInteger(String str) {
		if (CheckParam.isInteger(str)) {
			return Integer.parseInt(str);
		} else if (CheckParam.isNum(str)) {
			int index = str.indexOf(".");
			String integer = str.substring(0, index);
			return Integer.parseInt(integer);
		}
		return null;
	}


	/**
	 * 将一段英文的首字母变为大写(其他不变)
	 *
	 * @author huf
	 * @param str
	 * @return
	 */
	public static String toUpperFirstChar(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 定义一个过滤器进行去重
	 * 不涉及到共享变量，没有线程安全问题
	 * 为什么是这样写的，因为上面的filter是需要一个Predicate返回的参数的
	 * 用concurrentHashMap里面的putIfAbsent进行排重
	 */
	public static<T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>(16);
		return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
	}

	/**
	 * 返回真实IP
	 
	 * @date 2018/12/28 15:39
	 * @param request Servlet对象
	 * @return java.lang.String 真实IP
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String unknown = " unknown ";
		String ip = request.getHeader(" x-forwarded-for ");
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader(" Proxy-Client-IP ");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader(" WL-Proxy-Client-IP ");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static void main(String[] args) {

	}
}
