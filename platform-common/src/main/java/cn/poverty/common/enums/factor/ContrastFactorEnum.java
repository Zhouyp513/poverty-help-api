package cn.poverty.common.enums.factor;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 
 * @packageName cn.poverty.common.enums
 * @Description: 对比查询条件枚举
 * @date 2021-05-29
 */
@Getter
@AllArgsConstructor
public enum ContrastFactorEnum {

    /**
     * 等于
     */
    EQUALS ("EQUALS"),

    /**
     * 大于
     */
    GREATER_THAN ("GREATER_THAN"),

    /**
     * 大于或者等于
     */
    GREATER_THAN_OR_EQUAL_TO ("GREATER_THAN_OR_EQUAL_TO"),

    /**
     * 小于等于
     */
    LESS_THAN_OR_EQUAL_TO ("LESS_THAN_OR_EQUAL_TO"),

    /**
     * 小于
     */
    LESS_THAN ("LESS_THAN");

    /**
     * 状态码
     */
    private String code;

    public String getCode() {
        return code;
    }
}
