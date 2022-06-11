package cn.poverty.common.enums.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 
 * @packageName cn.poverty.common.enums
 * @Description: 物品状态枚举
 * @date 2021-02-16
 */
@Getter
@AllArgsConstructor
public enum ItemStatusEnum {

    /**
      * 物品状态-上架
      */
    ITEM_STATUS_ON ("ITEM_STATUS_ON","上架"),

    /**
     * 物品状态-下架
     */
    ITEM_STATUS_OFF("ITEM_STATUS_OFF","下架");

    /**
     * 状态码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
