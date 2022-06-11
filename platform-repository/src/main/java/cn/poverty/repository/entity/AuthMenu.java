package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;

/**
 * 菜单实体
 * @title: Menu.java
 * @author
 * @date 2019/4/24 11:13
 * @return
 */
@Entity
@Data
@Table(name = "auth_menu")
public class AuthMenu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5345514774473655446L;


    /**
     * 系统菜单主键ID
     */
    @Column(name = "auth_menu_id",nullable = false)
    private String authMenuId = SnowflakeIdWorker.uniqueSequenceStr();

    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    private String menuName;


    /**
     * 父级菜单ID
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 前端path / 即跳转路由
     */
    @Column(name = "path")
    private String path;


    /**
     * 对应Vue组件
     */
    @Column(name = "component")
    private String component;

    /**
     * 权限
     */
    @Column(name = "perms")
    private String perms;

    /**
     * 菜单图标
     */
    @Column(name = "menu_icon")
    private String menuIcon;

    /**
     * 菜单类型 0->按钮,1->菜单
     */
    @Column(name = "menu_type")
    private String menuType;

    /**
     * 序号
     */
    @Column(name = "order_num")
    private Integer orderNum;

}
