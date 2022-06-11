package cn.poverty.service;

import cn.poverty.common.entity.Tree;
import cn.poverty.interaction.req.menu.AuthMenuAddReq;
import cn.poverty.interaction.req.menu.AuthMenuTreeQueryReq;
import cn.poverty.interaction.req.menu.AuthMenuUpdateReq;
import cn.poverty.interaction.resp.menu.AuthMenuTreeQueryWrapResp;
import cn.poverty.interaction.resp.menu.AuthMenuTreeResp;

import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统菜单Service
 * @date 2019-08-26
 */
public interface AuthMenuService {


    /**
     * 更新菜单
     *
     * @date 2019-09-05
     * @param authMenuUpdateReq 更新菜单Req
     * @return
     */
    void updateMenu(AuthMenuUpdateReq authMenuUpdateReq);

    /**
     * 删除菜单
     *
     * @date 2019-09-05
     * @param authMenuIdList 需要被删除的菜单的主键ID
     */
    void deleteMenu(List<String> authMenuIdList);

    /**
     * 新增菜单
     *
     * @date 2019-09-05
     * @param authMenuAddReq 新增菜单请求参数
     */
    void addMenu(AuthMenuAddReq authMenuAddReq);

    /**
     * 权限菜单树集合查询
     *
     * @date 2019-08-26
     * @param authMenuTreeQueryReq 权限菜单树查询Req
     * @return AuthMenuTreeListQueryWrapResp

    AuthMenuTreeListQueryWrapResp queryMenuTreeList(AuthMenuTreeQueryReq authMenuTreeQueryReq); */


    /**
     * 查询部门树
     *
     * @date 2019-08-26
     * @param authMenuTreeQueryReq
     * @return AuthMenuTreeQueryWrapResp
     */
    Tree<AuthMenuTreeResp> queryAllAuthMenuTree(AuthMenuTreeQueryReq authMenuTreeQueryReq);

    /**
     * 查询所有部门树集合
     *
     * @date 2019-08-26
     * @param authMenuTreeQueryReq
     * @return AuthMenuTreeQueryWrapResp
     */
    AuthMenuTreeQueryWrapResp queryAllAuthMenuTreeList(AuthMenuTreeQueryReq authMenuTreeQueryReq);
}
