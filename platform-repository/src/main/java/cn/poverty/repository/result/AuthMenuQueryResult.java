package cn.poverty.repository.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: VUE路由(菜单)查询结果
 * @date 2019-08-22
 */
@Data
public class AuthMenuQueryResult implements Serializable {

    private static final long serialVersionUID = 6420782843611627075L;


    /**
      * 菜单主键ID
      */
    private String id;

    /**
     * 菜单业务主键ID
     */
    private String authMenuId;

    /**
     * 菜单父级ID
     */
    private String parentId;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * VUE组件
     */
    private String component;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 权限
     */
    private String perms;


}
