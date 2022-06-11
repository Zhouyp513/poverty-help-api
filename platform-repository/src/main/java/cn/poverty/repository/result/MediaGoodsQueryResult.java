package cn.poverty.repository.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 动态对应商品结果查询封装
 * @date 2019-11-10
 */
@Data
public class MediaGoodsQueryResult implements Serializable {

    private static final long serialVersionUID = -157345952839061056L;


    /**
     * 商品主键ID
     */
    private String goodsId;

    /**
      * 商品名称
      */
    private String goodsName;


    /**
     * 商品描述
     */
    private String goodsDesc;

}
