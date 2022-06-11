package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 用户系统配置实体
 * @date 2019-08-20
 */
@Entity
@Data
@Table(name="system_user_config")
public class AuthUserConfig extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 6146428772352848000L;


    /**
     * 用户系统配置主键ID
     */
    @Column(name = "system_user_config_id",nullable = false)
    private String systemUserConfigId = SnowflakeIdWorker.uniqueSequenceStr();


    /**
     * 用户业务主键ID
     */
    @Column(name = "auth_user_id",nullable = false)
    private String authUserId;

    /**
     * 系统主题 dark暗色风格，light明亮风格
     */
    @Column(name = "theme",nullable = false)
    private String theme;

    /**
     * 系统布局 side侧边栏，head顶部栏
     */
    @Column(name = "layout",nullable = false)
    private String layout;

    /**
     * 页面风格 1多标签页 0单页
     */
    @Column(name = "multi_page",nullable = false)
    private String multiPage;

    /**
     * 页面滚动是否固定侧边栏 1固定 0不固定
     */
    @Column(name = "fix_sider_bar",nullable = false)
    private Integer fixSiderBar;

    /**
     * 页面滚动是否固定顶栏 1固定 0不固定
     */
    @Column(name = "fix_header",nullable = false)
    private Integer fixHeader;

    /**
     * 主题颜色 RGB值
     */
    @Column(name = "color",nullable = false)
    private String color;

}
