package cn.poverty.interaction.resp.menu;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 菜单树Resp
 * @date 2019-08-26
 */
@Data
public class AuthMenuTreeResp implements Serializable {

    private static final long serialVersionUID = 6989699173601482445L;


    /**
     * 系统菜单主键ID
     */
    private String authMenuId;

    /**
     * 菜单名称
     */
    private String menuName;


    /**
     * 父级菜单ID
     */
    private String parentId;

    /**
     * 前端path / 即跳转路由
     */
    private String path;


    /**
     * 对应Vue组件
     */
    private String component;

    /**
     * 权限
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 菜单类型 0->按钮,1->菜单
     */
    private String menuType;

    /**
     * 序号
     */
    private Integer orderNum;

    /**
     * 创建时间
     */
    //@ApiModelProperty(value = "修改时间",notes ="修改时间" )
    @JSONField(format="yyyy-MM-dd HH:mm:ss",name = "updateTime")
    private LocalDateTime createTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());


    /**
     * 修改时间
     */
    //@ApiModelProperty(value = "修改时间",notes ="修改时间" )
    @JSONField(format="yyyy-MM-dd HH:mm:ss",name = "updateTime")
    private LocalDateTime updateTime;


}
