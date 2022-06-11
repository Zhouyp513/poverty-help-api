package cn.poverty.common.enums.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**

 * @packageName cn.poverty.common.enums
 * @Description: 系统板块枚举
 * @date 2021-03-24
 */
@Getter
@AllArgsConstructor
public enum PanelEnum {

    /**
     * 主页
     */
    MAIN ("MAIN","主页"),

    /**
     * 页面内
     */
    PAGE("PAGE","页面内");

    /**
     * 状态码
     */
    private String code;

    /**
     * 状态描述
     */
    private String description;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
