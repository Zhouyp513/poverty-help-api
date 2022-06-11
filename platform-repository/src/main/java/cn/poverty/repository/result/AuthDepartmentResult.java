package cn.poverty.repository.result;

import lombok.Data;

import java.io.*;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 部门查询结果
 * @date 2019-08-23
 */
@Data
public class AuthDepartmentResult implements Serializable {

    private static final long serialVersionUID = 3883563819407091556L;


    /**
     * 部门主键ID
     */
    private String departmentId;


    /**
     * 部门名称
     */
    private String departmentName;


    /**
     * 上级部门
     */
    private String parentId;

    /**
     * 排序
     */
    private Integer orderNum;



}
