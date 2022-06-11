package cn.poverty.repository.result;

import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poor.village.repository.result
 * @Description: 菜单查询结果封装
 * @date 2021-04-18
 */
@Data
public class AuthMenuResult implements Serializable {

    private static final long serialVersionUID = -4675041702927406639L;


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

}
