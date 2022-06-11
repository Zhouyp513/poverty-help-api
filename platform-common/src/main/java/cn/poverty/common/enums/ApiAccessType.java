package cn.poverty.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 
 * @packageName cn.poverty.common.enums
 * @Description: 系统API进入类型
 * @date 2021-01-21
 */
@Getter
@AllArgsConstructor
public enum ApiAccessType {


    /**
     * API平台的认证
     */
    API_ACCESS_TYPE_WEB("platformAuth","platformAuth"),

    /**
     * 手机APP的认证
     */
    API_ACCESS_TYPE_APP("appAuth","appAuth");

    /**
     * API访问code
     */
    private String code;

    /**
     * API访问类型
     */
    private String type;

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

}
