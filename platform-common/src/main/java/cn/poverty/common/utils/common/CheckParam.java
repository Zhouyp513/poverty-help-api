package cn.poverty.common.utils.common;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 
 * @packageName cn.poverty.common.utils.common
 * @Description: 参数检查类
 * @date 2021-01-21
 */
public class CheckParam {


	/**
	 * 长整型时间pattern
	 */
	private static final Pattern LONG_TIME_PATTERN = Pattern.compile("^1[0-9]{9}$");


	/**
	 * 判断集合是否为空
	 * @param list
	 * @return
	 */
	public static boolean isNull(List<Object> list){
		return isNull(list) && list.isEmpty();
	}

	/**
	 * 验证字符串是否为空
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isNull(String str) {
		return null == str || "".equals(str.trim()) || "null".equals(str.trim()) || "undefined".equals(str.trim());
	}

	/**
	 * 验证对象[字符串]是否为空
	 * @param obj
	 * @return 是：true，否：false
	 */
	public static boolean isNull(Object obj) {
		return null == obj || isNull(obj.toString());
	}

	/**
	 * 验证字符串是否为整数
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isInteger(String str) {
		return !isNull(str) && str.matches("^-?\\d+$");
	}

	/**
	 * 验证对象是否为整数
	 * @param obj
	 * @return 是：true，否：false
	 */
	public static boolean isInteger(Object obj) {
		return obj != null && obj.toString().matches("^-?\\d+$");
	}

	/**
	 * 验证字符串是否为身份证格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isIdCard(String str) {

		Integer idCardLength = 18;

		if (str == null || str.length() != idCardLength) {
			return false;
		}

		// 17位加权因子，与身份证号前17位依次相乘。
		int []  wArray = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		// 保存级数和
		int sum = 0;
		for (int i = 0; i < str.length() - 1; i++)
		{
			sum += new Integer(str.substring(i, i + 1)) * wArray[i];
		}
		/**
		 * 校验结果，上一步计算得出的结果与11取模，得到的结果相对应的字符就是身份证最后一位，也就是校验位。
		 * 例如：0对应下面数组第一个元素，以此类推。
		 */
		Integer modNumber = 11;
		String [] sumsArray  = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		if (sumsArray[(sum % Integer.valueOf(modNumber))].equals(str.substring(str.length() - 1, str.length())))
		{// 与身份证最后一位比较
			return true;
		}
		return false;
	}

	/**
	 * 验证字符串是否为护照格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isPassportCard(String str){
		return !isNull(str) && str.matches("(P\\d{7})|(G\\d{8})");
	}

	/**
	 * 验证字符串是否为导游证格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isGuideCard(String str){
		return !isNull(str) && str.matches("^D(-)?\\d{4}(-)?\\d{6}");
	}

	/**
	 * 验证字符串是否为手机号码
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isMobile(String str) {
		return !isNull(str) && str.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
	}

	/**
	 * 验证字符串是否为车牌号格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isBusNumber(String str) {
		return !isNull(str) && str.matches("^[\u4e00-\u9fa5|WJ]{1}[A-Z0-9]{6}$");
	}

	/**
	 * 验证字符串是否为电话号码[座机]格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isPhone(String str) {
		return !isNull(str) && str.matches("^(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$");
	}

	/**
	 * 验证字符串是否为浮点数
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isDouble(String str) {
		return !isNull(str) && str.matches("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$");
	}

	/**
	 * 验证字符串是否为Email格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isEmail(String str) {
		return !isNull(str) && str.matches("^[0-9a-z][a-z0-9\\._-]{1,}@[a-z0-9-]{1,}[a-z0-9]\\.[a-z\\.]{1,}[a-z]$");
	}

	/**
	 * 验证字符串是否为数值型
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isNum(String str) {
		return !isNull(str) && str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * 验证字符串是否为数值型
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isNotNullAndNotNum(String str) {
		return !isNull(str) && !isNum(str);
	}

	/**
	 * 验证对象是否为数值型
	 * @param obj
	 * @return 是：true，否：false
	 */
	public static boolean isNum(Object obj) {
		return obj != null && isNum(obj.toString());
	}


	/**
	 * 检查文件名否为图片 流方式
	 * @param f
	 * @return 是：true，否：false
	 */
	public final static String getImageFileType(File f) {
		if (isImage(f)) {
			try {
				ImageInputStream iis = ImageIO.createImageInputStream(f);
				Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
				if (!iter.hasNext()) {
					return null;
				}
				ImageReader reader = iter.next();
				iis.close();
				return reader.getFormatName();
			} catch (IOException e) {
				return null;
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 检查图片文件否为可伸缩
	 * @param file
	 * @return 是：true，否：false
	 */
	public static final boolean isImage(File file) {
		boolean flag = false;
		try {
			BufferedImage bufreader = ImageIO.read(file);
			int width = bufreader.getWidth();
			int height = bufreader.getHeight();
			if (width == 0 || height == 0) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (IOException e) {
			flag = false;
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}


	/**
	 * 验证字符串是否为yyyy-MM-dd格式的合法日期
	 * @param str
	 * @return boolean
	 * @author 谭军
	 */
	public static final boolean isDate(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 此设置用于验证日期的合法性
		sdf.setLenient(false);
		try {
			sdf.parse(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证字符串是否为yyyy-MM格式的日期
	 * @param str
	 * @return
	 */
	public static final boolean isDateYm(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		// 此设置用于验证日期的合法性
		sdf.setLenient(false);
		try {
			sdf.parse(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证字符串是否为yyyy格式的日期
	 * @param str
	 * @return
	 */
	public static final boolean isDateY(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		// 此设置用于验证日期的合法性
		sdf.setLenient(false);
		try {
			sdf.parse(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证字符串是否为yyyy-MM-dd HH:mm:ss格式的合法日期时间
	 * @param str
	 * @return boolean
	 * @author 谭军
	 */
	public static final boolean isDatetime(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 此设置用于验证日期的合法性
		sdf.setLenient(false);
		try {
			sdf.parse(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证对象是否为日期时间
	 * @param obj
	 * @return boolean
	 * @author 谭军
	 */
	public static final boolean isDatetime(Object obj){
		return obj != null && isDatetime(obj.toString());
	}

	/**
	 * 验证字符串是否为yyyy-MM-dd HH:mm格式的合法日期时间
	 * @param str
	 * @return boolean
	 * @author 谭军
	 */
	public static final boolean isDatetimeNoSecond(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 此设置用于验证日期的合法性
		sdf.setLenient(false);
		try {
			sdf.parse(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证对象是否为合法的日期格式
	 * @param obj 验证对象
	 * @param pattern 匹配格式
	 * @return boolean
	 * @author 谭军
	 */
	public static final boolean isDateTime(Object obj,String pattern){
		if(obj == null || pattern == null){
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		// 此设置用于验证日期的合法性
		sdf.setLenient(false);
		try {
			if(obj.getClass() == Date.class || obj instanceof Date){
				Date date = (Date) obj;
				sdf.format(date);
			}else{
				sdf.parse(obj.toString());
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}


	/**
	 * 判断Long型时间戳是否符合规范 (暂时不支持毫秒时间戳)
	 * @param time Long型时间戳
	 * @return boolean
	 * @author jimmy
	 */
	public static final Boolean isLongTime(Long time){
		String time2 = time.toString();
		Matcher m = LONG_TIME_PATTERN.matcher(time2);
		return m.matches();
	}


	public static void main(String[] args) {

	}
}
