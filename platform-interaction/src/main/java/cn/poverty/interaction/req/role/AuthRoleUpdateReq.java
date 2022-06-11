package cn.poverty.interaction.req.role;

import cn.poverty.common.validation.NotEmpty;
import lombok.Data;

import java.io.*;
import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 角色请求更新Req
 * @date 2019-09-04
 */
@Data
public class AuthRoleUpdateReq implements Serializable {


    private static final long serialVersionUID = -5566089276103424980L;

    /**
     * 业务主键ID
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String authRoleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色代码
     */
    private String roleCode;

    /**
     * 是否可选 01 是 02 否
     */
    private String optionalStatus;

    /**
     * 角色描述
     */
    private String remark;

    /**
     * 角色菜单集合
     */
    private List<String> authMenuIdList;
}
