package cn.poverty.interaction.req.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 用户系统个性定制Req
 * @date 2019-08-30
 */
@Data
public class AuthUserConfigUpdateReq implements Serializable {


    private static final long serialVersionUID = -191183623855560733L;


    /**
     * 用户系统配置主键ID
     */
    private String systemUserConfigId;


    /**
     * 用户业务主键ID
     */
    private String authUserId;

    /**
     * 系统主题 dark暗色风格，light明亮风格
     */
    private String theme;

    /**
     * 系统布局 side侧边栏，head顶部栏
     */
    private String layout;

    /**
     * 页面风格 1多标签页 0单页
     */
    private String multiPage;

    /**
     * 页面滚动是否固定侧边栏 1固定 0不固定
     */
    private Integer fixSiderBar;

    /**
     * 页面滚动是否固定顶栏 1固定 0不固定
     */
    private Integer fixHeader;

    /**
     * 主题颜色 RGB值
     */
    private String color;


}
