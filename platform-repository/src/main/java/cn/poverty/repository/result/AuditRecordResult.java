package cn.poverty.repository.result;

import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poverty.repository.result
 * @Description: 审核记录查询结果
 * @date 2021-05-26
 */
@Data
public class AuditRecordResult implements Serializable {

    private static final long serialVersionUID = -2009748231250159778L;

    /**
     * 业务主键ID
     */
    private String  auditRecordId;

    /**
     * 审核流程类型 跟随系统枚举
     */
    private String auditType;

    /**
     * 关联的其他数据的ID
     */
    private String itemId;

    /**
     * 审核状态 跟随系统枚举
     */
    private String  auditStatus;

    /**
     * 当前步骤
     */
    private String  currentStep;

    /**
     * 操作人手机号
     */
    private String operatorPhone;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 审核备注
     */
    private String  auditRemark;


}
