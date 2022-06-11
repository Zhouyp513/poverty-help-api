package cn.poverty.repository.result;

import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poverty.repository.result
 * @Description: 业务字段查询结果封装
 * @date 2021-04-22
 */
@Data
public class BusinessDictResult implements Serializable {

    private static final long serialVersionUID = -2421013475606408595L;


    /**
     * 系统字典主键ID
     */
    private String businessDictionaryId;

    /**
     * 业务字典类型
     */
    private String  dictType;

    /**
     * 键
     */
    private String  dictKey;

    /**
     * 值
     */
    private String  dictValue;

    /**
     * 排序字段
     */
    private Integer sortIndex;

    /**
     * 字典备注
     */
    private String dictRemark;
}
