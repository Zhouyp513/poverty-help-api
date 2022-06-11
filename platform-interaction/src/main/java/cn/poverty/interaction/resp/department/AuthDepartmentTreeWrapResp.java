package cn.poverty.interaction.resp.department;


import cn.poverty.common.entity.Tree;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 部门树查询Resp
 * @date 2019-08-26
 */
@Data
public class AuthDepartmentTreeWrapResp implements Serializable {

    private static final long serialVersionUID = -3763138947948705935L;


    /**
     * 数据
     */
    @JSONField(name = "rows")
    private Tree<AuthDepartmentTreeResp> trees;

    /**
     * 总数量
     */
    private Integer total;


}
