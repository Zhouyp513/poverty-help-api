package cn.poverty.interaction.resp.statistics;

import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 访问量统计Resp
 * @date 2019-08-23
 */
@Data
public class VisitCountResp implements Serializable {

    private static final long serialVersionUID = 7447015171852340899L;


    /**
      * 当天访问数量
      */
    private Long count;


    /**
     * 日期
     */
    private String days;

}
