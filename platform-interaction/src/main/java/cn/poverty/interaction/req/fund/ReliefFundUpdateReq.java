package cn.poverty.interaction.req.fund;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author
 * @packageName cn.poverty.interaction.req.fund
 * @Description: 救助金更新Req
 * @date 2021-10-20
 */
@Data
public class ReliefFundUpdateReq implements Serializable {


    private static final long serialVersionUID = 7047569245722584765L;


    /**
     * 业务主键ID
     */
    private String  reliefFundId;

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

    /**
     * 发放时间
     */
    @JSONField(format="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDateTime grantTime;
}
