package cn.poverty.common.enums.auth.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author
 * @packageName cn.poverty.common.enums
 * @Description: 角色数据隔离枚举
 * @date 2021-05-25
 */
@Getter
@AllArgsConstructor
public enum DataRoleCodeEnum {

    /**
     * 普通管理员
     */
    ADMIN("ADMIN"),

    /**
     * 普通管理员
     */
    COMMON_ADMIN ("COMMON_ADMIN"),

    /**
     * 工作人员
     */
    WORK_ROLE("WORK_ROLE"),

    /**
     * 贫困用户
     */
    POVERTY_MAN("POVERTY_MAN");


    /**
     * 状态码
     */
    private String code;

    public String getCode() {
        return code;
    }
}
