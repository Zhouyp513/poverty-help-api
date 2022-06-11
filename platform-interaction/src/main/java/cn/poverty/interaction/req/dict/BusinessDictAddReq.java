package cn.poverty.interaction.req.dict;

import cn.poverty.common.validation.NotEmpty;
import lombok.Data;

import java.io.*;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 新增系统业务字典
 * @date 2020-07-07
 */
@Data
public class BusinessDictAddReq implements Serializable {

    private static final long serialVersionUID = 7144384296679404691L;

    /**
     * 系统字典主键ID
     */
    private String businessDictionaryId;

    /**
     * 业务字典类型
     */
    @NotEmpty(message = "业务字典类型->不可为空")
    private String  dictType;

    /**
     * 键
     */
    @NotEmpty(message = "键->不可为空")
    private String  dictKey;

    /**
     * 值
     */
    @NotEmpty(message = "值->不可为空")
    private String  dictValue;

    /**
     * 排序字段
     */
    @NotEmpty(message = "排序字段->不可为空")
    private Integer sortIndex;

    /**
     * 字典备注
     */
    private String dictRemark;


}
