package cn.poverty.common.enums.auth.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 
 * @packageName cn.poverty.common.enums
 * @Description: 数据权限参数名称的枚举
 * @date 2021-05-08
 */
@Getter
@AllArgsConstructor
public enum AuthDataParamEnum {

    /**
     * 地区字段名称
     */
    REGION_ID ("regionId"),

    /**
     * 企业ID字段名称
     */
    CORP_BASE_DATA_ID("corpBaseDataId");

    /**
     * 状态码
     */
    private String code;


    public String getCode() {
        return code;
    }

}
