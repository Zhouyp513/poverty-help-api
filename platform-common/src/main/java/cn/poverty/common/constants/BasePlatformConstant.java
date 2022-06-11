package cn.poverty.common.constants;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 基础业务配置
 * @date 2019-08-20
 */
@Data
@Component
public class BasePlatformConstant {


    /**
     * 用户锁定状态 1:锁定,2:有效
     */
    public static final Integer STATUS_LOCK = BigInteger.ONE.intValue();

    /**
     * 默认头像
     */
    public static final String DEFAULT_AVATAR = "default-avatar.png";

    /**
     * 系统默认颜色
     */
    public static final String DEFAULT_USER_CONFIG_COLOR = "rgb(24, 144, 255)";

    /**
     * 页面滚动是否固定顶栏 1固定 0不固定
     */
    public static final Integer DEFAULT_USER_FIX_HEADER = 1;

    /**
     * 页面滚动是否固定侧边栏 1固定 0不固定
     */
    public static final Integer DEFAULT_USER_FIX_SIDE_BAR = 1;

    /**
     * 系统布局 side侧边栏，head顶部栏
     */
    public static final String DEFAULT_USER_LAYOUT = "side";

    /**
     * 页面风格 1多标签页 0 单页
     */
    public static final String DEFAULT_USER_MULTI_PAGE = "1";

    /**
     * 系统主题 dark暗色风格，light明亮风格
     */
    public static final String DEFAULT_USER_THEME = "light";

    /**
     * 性别-男士
     */
    public static final String SEX_MALE = "male";

    /**
     * 性别-女士
     */
    public static final String SEX_FEMALE = "female";

    /**
     * 性别-未知
     */
    public static final String SEX_UNKNOWN = "2";


    /**
     * 系统菜单类型->按钮
     */
    public static final String TYPE_BUTTON = "1";

    /**
     * 系统菜单类型->菜单
     */
    public static final String TYPE_MENU = "0";
}
