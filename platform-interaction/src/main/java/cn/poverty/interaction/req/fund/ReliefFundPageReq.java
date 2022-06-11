package cn.poverty.interaction.req.fund;

import cn.poverty.common.interaction.base.data.BaseDataPageReq;
import lombok.Data;

import java.io.*;
import java.math.BigDecimal;

/**
 
 * @packageName cn.poverty.interaction.req.fund
 * @Description: 救助金分页查询
 * @date 2021-10-20
 */
@Data
public class ReliefFundPageReq extends BaseDataPageReq implements Serializable {


    private static final long serialVersionUID = -3908189275413141524L;


    /**
     * 救助金名称
     */
    private String  itemName;

    /**
     * 救助金金额
     */
    private BigDecimal fundAmount;

    /**
     * 救助金类型
     */
    private String  fundType;

    /**
     * 救助金等级
     */
    private String  fundLevel;

}
