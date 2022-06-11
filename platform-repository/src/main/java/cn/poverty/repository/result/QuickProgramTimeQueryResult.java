package cn.poverty.repository.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 秒杀配置查询结果
 * @date 2020-01-02
 */
@Data
public class QuickProgramTimeQueryResult implements Serializable {

    private static final long serialVersionUID = -4490675722910084142L;

    /**
     * 秒杀配置主键ID
     */
    private String  quickProgramTimeId;

    /**
     * 方案名称
     */
    private String  quickProgramName;

    /**
     * 秒杀开始时间
     */
    private String  quickProgramTime;

    /**
     * 秒杀结束时间
     */
    private String  quickProgramEndTime;

    /**
     * 秒杀时间配置类型 1 每天 2 每个月 3 每周
     */
    private Integer  quickProgramType;


    /**
     * 秒杀时间配置主键ID
     */
    private String  quickPurchaseTimeId;

    /**
     * 商品ID
     */
    private String  goodsId;

    /**
     * 秒杀商品总数量
     */
    private Integer  goodsPurchaseNum;


}
