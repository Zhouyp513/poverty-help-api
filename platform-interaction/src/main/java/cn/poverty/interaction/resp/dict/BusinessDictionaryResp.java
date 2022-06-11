package cn.poverty.interaction.resp.dict;

import lombok.Data;

import java.io.*;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 业务字典分页查询Resp
 * @date 2020-06-30
 */
@Data
public class BusinessDictionaryResp implements Serializable {

    private static final long serialVersionUID = -338929730219622442L;

    /**
     * 系统字典主键ID
     */
    private String  businessDictionaryId;

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
