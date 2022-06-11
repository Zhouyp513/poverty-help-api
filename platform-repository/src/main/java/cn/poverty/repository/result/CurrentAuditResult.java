package cn.poverty.repository.result;

import lombok.Data;

import java.io.*;

/**

 * @packageName cn.poverty.repository.result
 * @Description: 当前审核状态查询结果
 * @date 2021-05-24
 */
@Data
public class CurrentAuditResult implements Serializable {


    private static final long serialVersionUID = 8710334014189795288L;

    /**
     * 业务主键ID
     */
    private String  currentAuditId;

    /**
     * 对应的审核流程表ID
     */
    private String  auditProcessId;

    /**
     * 审核流程类型 跟随系统枚举
     */
    private String auditType;

    /**
     * 关联的其他数据的ID
     */
    private String itemId;

    /**
     * 当前步骤
     */
    private String  currentStep;

    /**
     * 审核状态 通过 01 驳回 02 草稿 03
     */
    private String auditStatus;

}
