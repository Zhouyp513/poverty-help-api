package cn.poverty.interaction.req.department;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 部门新增Req
 * @date 2019-08-30
 */
@Data
public class DepartmentAddReq implements Serializable {

    private static final long serialVersionUID = -6667970903884821201L;


    /**
     * 部门名称
     */
    private String departmentName;


    /**
     * 上级部门
     */
    private String parentId;

    /**
     * 排序
     */
    private Integer orderNum;


}
