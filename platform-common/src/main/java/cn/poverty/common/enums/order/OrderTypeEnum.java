package cn.poverty.common.enums.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 订单类型枚举
 * @author: 26092
 * @Date: 2019/4/27 18:44
 */
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {


    /**
     * 商品
     */
    GOODS_ITEM("GOODS_ITEM");


    /**
     * 订单代码
     */
    private String code;


    public String getCode() {
        return code;
    }
}
