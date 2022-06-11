package cn.poverty.common.constants;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 
 * @packageName cn.poverty.common.aspect
 * @Description: 业务配置
 * @date 2021-01-21
 */
@Data
@Component
public class PlatformConstant extends BasePlatformConstant {

    /**
     * 业务数据缓存时间
     */
    public static final Long CACHE_TIME = 10L;

    /**
     * 编辑器图片缓存前缀
     */
    public static final String EDITOR_IMAGE_PREFIX = "editorImage";

    /**
     * 商品SKU缓存前缀
     */
    public static final String GOODS_SKU_CACHE_PREFIX = "goods_sku_cache_prefix:";

    /**
     * 用户第三方平台绑定类型-微信
     */
    public static final String OTHER_PLATFORM_BIND_TYPE_WE_CHAT = "weChat";

    /**
     * 用户简要信息缓存前缀
     */
    public static final String APP_USER_SKETCH_CACHE_PREFIX = "app_user_sketch_cache_prefix:";

    /**
     * 首页banner信息缓存前缀
     */
    public static final String BANNER_CACHE_PREFIX = "banner_cache_prefix:";

    /**
     * 文章信息缓存前缀
     */
    public static final String ARTICLE_CACHE_PREFIX = "article_cache_prefix";

    /**
     * 后台系统API信息缓存前缀
     */
    public static final String ACTIVITY_CACHE_PREFIX = "activity_cache_prefix:";

    /**
     * 用户微信绑定的信息缓存前缀
     */
    public static final String WX_USER_INFO_BIND_STATUS_CACHE_PREFIX = "wx_user_bind_info_cache_prefix:";

    /**
     * 微信支付私钥缓存前缀
     */
    public static final String WX_PAY_SIGN_PRIVATE_KEY_CACHE_PREFIX = "wx_pay_sign_private_key_cache_prefix:";

    /**
     * 图片基元信息缓存前缀
     */
    public static final String IMAGE_META_CACHE_PREFIX = "image_meta_cache_prefix:";

    /**
     * 微信AccessToken信息缓存前缀
     */
    public static final String WX_ACCESS_TOKEN_CACHE_PREFIX = "wx_access_token_cache_prefix:";

    /**
     * 系统物品信息缓存前缀
     */
    public static final String ITEM_DATA_CACHE_PREFIX = "item_data_cache_prefix:";

    /**
     * 单独用户的系统物品信息缓存前缀
     */
    public static final String AUTH_USER_ITEM_DATA_CACHE_PREFIX = "auth_user_item_data_cache_prefix:{}:{}";

    /**
     * 系统物品海报背景信息缓存前缀
     */
    public static final String ITEM_POSTER_BACKGROUND_CACHE_PREFIX = "item_poster_background_cache_prefix";

    /**
     * 系统物品页面微信二维码信息缓存前缀
     */
    public static final String ITEM_PAGE_WX_QR_CODE_CACHE_PREFIX = "item_page_wx_qr_code_cache_prefix:";

    /**
     * 图片的Base64缓存前缀
     */
    public static final String IMAGE_BASE64_CACHE_PREFIX = "image_base64_cache_prefix:";

    /**
     * 物品海报缓存前缀
     */
    public static final String ITEM_POSTER_CACHE_PREFIX = "item_poster_cache_prefix:";

    /**
     * 系统物品查看次数缓存前缀
     */
    public static final String ITEM_VIEW_SUM_PREFIX = "item_view_sum_prefix:";

    /**
     * 所有业务字典缓存
     */
    public static final String ALL_DICT_CACHE_PREFIX = "all_dict_cache_prefix:";

    /**
     * 地理位置缓存
     */
    public static final String MERCHANT_POS = "merchant_pos";

    /**
     * 地图POI查询缓存
     */
    public static final String MAP_API_POI = "map_api_poi:";

    /**
     * 地区查询Code
     */
    public static final String DISTRICT_CODE = "district_code:";

    /**
     * 问卷网的签名值
     */
    public static final String WEN_JUAN_SIGN_KEY = "wen_juan_sign_login_key_cache_prefix:";

    /**
     * 总资金字典值
     */
    public static final String TOTAL_FUND_AMOUNT = "totalFundAmount";

}



