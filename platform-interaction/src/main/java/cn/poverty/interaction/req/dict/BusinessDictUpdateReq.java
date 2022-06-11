package cn.poverty.interaction.req.dict;

import cn.poverty.common.interaction.base.page.BasePageReq;
import cn.poverty.common.validation.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统业务字典分页查询Req
 * @date 2020-07-07
 */
@Data
public class BusinessDictUpdateReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 6341081874728412289L;


    /**
     * 系统字典主键ID
     */
    @NotEmpty(message = "系统字典主键ID->不可为空")
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
