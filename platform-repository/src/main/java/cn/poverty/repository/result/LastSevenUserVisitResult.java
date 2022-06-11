package cn.poverty.repository.result;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 最近七位用户访问统计结果
 * @date 2019-08-22
 */
public class LastSevenUserVisitResult implements Serializable {

    /**
      * 访问次数
      */
    private Integer count;


    /**
     * 访问日期
     */
    private Integer days;
}
