package cn.poverty.common.enums.auth.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**

 * @packageName cn.poverty.common.enums
 * @Description: 用户简略信息类型枚举
 * @date 2021-05-25
 */
@Getter
@AllArgsConstructor
public enum  SketchOtherTypeEnum {

    /**
     * 商户
     */
    MERCHANT("MERCHANT");

    /**
     * 状态码
     */
    private String code;


    public String getCode() {
        return code;
    }
}
