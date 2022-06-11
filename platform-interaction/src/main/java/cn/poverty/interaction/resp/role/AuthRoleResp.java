package cn.poverty.interaction.resp.role;

import cn.poverty.common.interaction.base.BaseResp;
import lombok.Data;

import java.io.*;

/**
 * @author
 * @packageName cn.poverty.interaction.resp.role
 * @Description: 角色Resp
 * @date 2021-04-06
 */
@Data
public class AuthRoleResp extends BaseResp implements Serializable {


    private static final long serialVersionUID = 4551051533141642247L;


    /**
     * 系统角色主键ID
     */
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
     * 角色描述
     */
    private String remark;

}
