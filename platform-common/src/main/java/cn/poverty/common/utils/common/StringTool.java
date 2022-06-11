package cn.poverty.common.utils.common;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**

 * @packageName cn.poverty.common.utils.common
 * @Description: 字符串工具类
 * @date 2021-01-21
 */
public class StringTool {

    /**
     * 利用正则删除下划线，把下划线后一位改成大写
     */
    private static final Pattern PATTERN = Pattern.compile("_(\\w)");

    /**
     * 匹配英文字母
     */
    private static final Pattern PATTERN_A_Z = Pattern.compile("[A-Z]");

    /**
     * @author Lcc
     * @Desc 下划线转驼峰
     * @version 2018/7/17 上午11:40
     * @Param [str]
     * @return java.lang.StringBuffer
     **/
    public static StringBuffer camel(StringBuffer str) {

        Matcher matcher = PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if(matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        }else {
            return sb;
        }
        return camel(sb);
    }


    /**
     * @author Lcc
     * @Desc 驼峰转下划线
     * @version 2018/7/17 上午11:40
     * @Param [str]
     * @return java.lang.StringBuffer
     **/
    public static StringBuffer underline(StringBuffer str) {

        Matcher matcher = PATTERN_A_Z.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if(matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb,"_"+matcher.group(0).toLowerCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        }else {
            return sb;
        }
        return underline(sb);
    }
}
