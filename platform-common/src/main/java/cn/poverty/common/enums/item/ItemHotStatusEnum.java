package cn.poverty.common.enums.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 
 * @packageName cn.poverty.common.enums
 * @Description: 物品热门状态枚举
 * @date 2021-02-16
 */
@Getter
@AllArgsConstructor
public enum ItemHotStatusEnum {

    /**
      * 物品热门状态-上架
      */
    HOT ("HOT","热门"),

    /**
     * 物品热门状态-下架
     */
    UN_HOT("UN_HOT","非热门");

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
