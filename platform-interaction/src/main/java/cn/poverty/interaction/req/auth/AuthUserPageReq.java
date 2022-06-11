package cn.poverty.interaction.req.auth;

import cn.poverty.common.interaction.base.page.BasePageReq;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统用户分页查询Req
 * @date 2019-08-21
 */
@Data
public class AuthUserPageReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = -2056842924949785613L;


    /**
      * 用户名称
      */
    private String userName;


    /**
     * 部门ID
     */
    private String departmentId;

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

    /**
     * 是否需要数据
     */
    private Boolean needData = Boolean.FALSE;

    /**
     * 角色Code
     */
    private String roleCode;


}
