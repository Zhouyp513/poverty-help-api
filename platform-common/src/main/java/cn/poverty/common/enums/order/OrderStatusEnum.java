package cn.poverty.common.enums.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 订单状态枚举
 * @author: 26092
 * @Date: 2019/4/27 18:44
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    /**
     * 待付款
     */
    UN_PAY ("待付款","UN_PAY"),

    /**
     * 待使用
     */
    UN_USE("待使用","UN_USE"),

    /**
     * 待发货
     */
    UN_SEND("待发货","UN_SEND"),

    /**
     * 待收货
     */
    UN_TAKE("待收货","UN_TAKE"),

    /**
     * 待评价
     */
    UN_EVALUATE("待评价","UN_EVALUATE"),

    /**
     * 交易成功
     */
    SUCCESS("交易成功","SUCCESS"),

    /**
     * 退款中
     */
    REFUND_ING("退款中","REFUND_ING"),

    /**
     * 退款
     */
    REFUNDED("退款","REFUNDED"),

    /**
     * 取消
     */
    CANCEL("取消","CANCEL");



    /**
     * 订单状态描述
     */
    private String description;

    /**
     * 订单状态码
     */
    private String code;


    public String getDescription() {
        return description;
    }


    public String getCode() {
        return code;
    }
}
