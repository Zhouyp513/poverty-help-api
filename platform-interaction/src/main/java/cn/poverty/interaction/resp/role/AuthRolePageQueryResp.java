package cn.poverty.interaction.resp.role;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.*;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统角色分页查询Resp
 * @date 2019-08-23
 */
@Data
public class AuthRolePageQueryResp implements Serializable {

    private static final long serialVersionUID = -6029852866683022561L;


    /**
     * 角色ID
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
     * 是否可选 01 是 02 否
     */
    private String optionalStatus;

    /**
     * 备注
     */
    private String remark;


    /**
     * 创建时间
     */
    //@ApiModelProperty(value = "修改时间",notes ="修改时间" )
    @JSONField(format="yyyy-MM-dd HH:mm:ss",name = "createTime")
    private LocalDateTime createTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());


    /**
     * 修改时间
     */
    //@ApiModelProperty(value = "修改时间",notes ="修改时间" )
    @JSONField(format="yyyy-MM-dd HH:mm:ss",name = "updateTime")
    private LocalDateTime updateTime;


}
