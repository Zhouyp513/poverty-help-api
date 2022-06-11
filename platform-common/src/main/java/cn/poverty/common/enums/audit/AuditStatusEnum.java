package cn.poverty.common.enums.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 
 * @packageName cn.poverty.common.enums
 * @Description: 整体流程状态的枚举
 * @date 2021-05-08
 */
@Getter
@AllArgsConstructor
public enum AuditStatusEnum {

    /**
     * 通过
     */
    PASS ("PASS"),

    /**
     * 拒绝
     */
    REJECT("REJECT"),

    /**
     * 待审核
     */
    WAIT_HANDLE("WAIT_HANDLE"),

    /**
     * 已经发放
     */
    GRANTED("GRANTED");

    /**
     * 状态码
     */
    private String code;

    public String getCode() {
        return code;
    }

}
