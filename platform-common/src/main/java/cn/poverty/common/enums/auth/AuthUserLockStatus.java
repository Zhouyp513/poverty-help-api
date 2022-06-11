package cn.poverty.common.enums.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author
 * @packageName cn.poverty.common.enums
 * @Description: 系统用户锁定状态枚举
 * @date 2021-04-06
 */
@Getter
@AllArgsConstructor
public enum AuthUserLockStatus {

    /**
     * 用户状态-锁定
     */
    LOCKED ("锁定",1),

    /**
     * 用户状态-有效
     */
    UN_LOCKED("有效",2);

    /**
     * 状态描述
     */
    private String description;

    /**
     * 状态码
     */
    private Integer code;

    public String getDescription() {
        return description;
    }


    public Integer getCode() {
        return code;
    }
}
