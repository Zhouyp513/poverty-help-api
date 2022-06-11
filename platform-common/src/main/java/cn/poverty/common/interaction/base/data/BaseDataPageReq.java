package cn.poverty.common.interaction.base.data;

import cn.poverty.common.interaction.base.page.BasePageReq;
import lombok.Data;

import java.io.*;
import java.util.List;

/**
 
 * @packageName cn.poverty.interaction.base.auth
 * @Description: 基本数据分页查询Req
 * @date 2021-05-09
 */
@Data
public class BaseDataPageReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 3689769231060249079L;

    /**
     * 数据开始时间
     */
    private String beginTime;

    /**
     * 数据结束时间
     */
    private String endTime;

    /**
     * 对比查询条件
     */
    private List<ContrastReq> contrastList;

    /**
     * 正序排序字段名
     */
    private String ascSortName;

    /**
     * 倒序排序字段名
     */
    private String descSortName;

    /**
     * 排序方法
     */
    private String  sortMethod;
}
