package cn.poverty.common.interaction.base.page;

import lombok.Data;

import java.io.Serializable;

/**
 * 基础的分页查询Req
 
 * 创建时间：2018/5/9 下午10:11
 */
@Data
public class BasePageQueryReq extends BasePageReq implements Serializable {


    private static final long serialVersionUID = -2881255032015837110L;

    /**
     * 当前页码数
     */
    //@ApiModelProperty(value = "当前页码数",required = true,notes = "当前页码数",example = "1")
    private Integer page = 1;

    /**
     * 每页条数
     */
    //@ApiModelProperty(value = "每页条数",required = true,notes = "每页条数",example = "10")
    private Integer size = 10;


}
