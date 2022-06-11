package cn.poverty.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**

 * @packageName cn.poverty.common.enums
 * @Description: 系统错误码枚举
 * @date 2021-01-21
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {


    /**
     * 成功
     */
    SUCCESS("200","成功"),

    /**
     * 成功
     */
    SUCCESS_VUE("100","成功"),

    /**
     * 参数格式错误
     */
    PARAM_ERROR("000004","参数格式错误"),

    /**
     * 业务流程异常,拒绝处理
     */
    BUSINESS_REFUSE("403", "业务流程异常,拒绝处理"),

    /**
     * 业务处理异常
     */
    ERROR("500","业务处理异常"),

    /**
     * 登录失效,需要验证登录
     */
    LOGIN_ERROR_CODE("501","登录失效,需要验证登录"),

    /**
     * 参数缺失
     */
    NO_REQUIRED_PARAM_ERROR("0000005","参数缺失"),

    /**
     * 用户认证信息丢失
     */
    NO_AUTH_META_ERROR("0000006","用户认证信息丢失"),

    /**
     * 认证错误
     */
    AUTH__ERROR("0000007","认证错误"),

    /**
     * 没有权限
     */
    E_502("0000008","没有权限"),

    /**
     * 旧密码输入错误
     */
    ERROR_OLD_PASSWORD("0000009","旧密码输入错误"),

    /**
     * 修改密码失败
     */
    ERROR_UPDATE_PASSWORD("0000010","修改密码失败"),

    /**
     * 当前用户还不是商户
     */
    ERROR_NOT_MERCHANT("0000011","当前用户还不是商户"),

    /**
     * 商品不存在
     */
    ERROR_MERCHANT_NOT_EXIST("0000012","商品不存在"),

    /**
     * 分销金额错误
     */
    ERROR_GOODS_REBATE_AMOUNT_ERROR("0000013","分销金额错误"),

    /**
     * 用户支付宝信息查询异常
     */
    USER_ALI_PAY_AUTH_CODE_ERROR("000014","用户支付宝信息查询异常"),

    /**
     * 用户名已经存在
     */
    AUTH_USER_EXIST_CODE_ERROR("000015","用户名已经存在"),

    /**
     * 旧密码校验失败
     */
    OLD_PASSWORD_VERIFY_PASSWORD("0000016","旧密码校验失败"),

    /**
     * 重复的角色代码
     */
    REPEAT_ROLE_CODE_ERROR("0000017","角色代码重复"),

    /**
     * 角色Code不存在
     */
    ROLE_CODE_NOT_EXIST_ERROR("0000019","角色Code不存在,请设置"),

    /**
     * 已经申请了该项目
     */
    APPLIED_ITEM_ERROR("0000020","已经申请了该项目"),

    /**
     * 数据填写有误
     */
    NO_IMPORT_DATA_ERROR("0000021","数据填写有误，请检查后重新填写。"),

    ;

    /**
     * 错误码code
     */
    private String code;

    /**
     * 错误信息
     */
    private String message;


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
