package cn.poverty.interaction.resp.statistics;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统访问统计Resp
 * @date 2019-08-22
 */
@Data
public class StatisticsVisitCountResp implements Serializable {


    /**
      * 访问总量
      */
    private Integer totalVisitCount;


    /**
     * 今日访问总量
     */
    private Integer todayVisitCount;


    /**
     * 今日访问IP总量
     */
    private Integer todayIp;

    /**
     * 过去七天访问总量
     */
    private List<VisitCountResp> lastSevenVisitCount;


    /**
     * 当前用户最近的访问记录
     */
    private List<VisitCountResp> lastSevenUserVisitCount;

}
