package cn.poverty.common.interaction.base.data;

import cn.poverty.common.enums.factor.ContrastFactorEnum;
import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poverty.interaction.req
 * @Description: 对比条件查询Req
 * @date 2021-05-29
 */
@Data
public class ContrastReq implements Serializable {


    private static final long serialVersionUID = 8036908867760142301L;

    /**
      * 字段名称
      */
    private String itemName;

    /**
     * 字段对比条件
     */
    private ContrastFactorEnum contrastFactor;

    /**
     * 字段对比值
     */
    private String contrastValue;

}
