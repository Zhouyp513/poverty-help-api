package cn.poverty.common.enums.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 
 * @packageName cn.poverty.common.enums
 * @Description: 系统物品类型
 * @date 2021-03-03
 */
@Getter
@AllArgsConstructor
public enum ItemTypeEnum {

    //物品类型(GOODS/商品)
    GOODS_ITEM("商品","GOODS");

    /**
     * 收藏类型
     */
    private String type;


    /**
     * 收藏类型CODE
     */
    private String code;


    public String getType() {
        return type;
    }


    public String getCode() {
        return code;
    }
}
