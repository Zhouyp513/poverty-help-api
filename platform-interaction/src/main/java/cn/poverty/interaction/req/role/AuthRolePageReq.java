package cn.poverty.interaction.req.role;

import cn.poverty.common.interaction.base.page.BasePageReq;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统角色分页查询Req
 * @date 2019-08-23
 */
@Data
public class AuthRolePageReq extends BasePageReq implements Serializable {


    private static final long serialVersionUID = 7627935093750626139L;


    /**
      * 角色名称
      */
    private String roleName;

    /**
     * 是否需要查询所有数据
     */
    private Boolean needAll = false;

    /**
     * 角色代码
     */
    private String roleCode;


    /**
     * 是否可选 01 是 02 否
     */
    private String optionalStatus;

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
