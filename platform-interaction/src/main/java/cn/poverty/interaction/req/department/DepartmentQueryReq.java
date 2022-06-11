package cn.poverty.interaction.req.department;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.*;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 部门分页查询Req
 * @date 2019-08-23
 */
@Data
public class DepartmentQueryReq implements Serializable {


    private static final long serialVersionUID = -1919874966521808573L;


    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 开始创建时间
     */
    @JSONField(format="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String beginTime;

    /**
     * 结束创建时间
     */
    @JSONField(format="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String endTime;

}
