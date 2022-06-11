package cn.poverty.service;

import cn.poverty.interaction.req.role.AuthRoleAddReq;
import cn.poverty.interaction.req.role.AuthRolePageReq;
import cn.poverty.interaction.req.role.AuthRoleUpdateReq;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.interaction.resp.role.AuthRolePageQueryResp;

import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统角色Service
 * @date 2019-08-23
 */
public interface AuthRoleService {


    /**
     * 分页查询用户角色
     *
     * @date 2019-08-23
     * @param authRolePageReq 分页查询用户角色参数
     * @return Pagination
     */
    Pagination<AuthRolePageQueryResp> queryAuthRoleByPage(AuthRolePageReq authRolePageReq);


    /**
     * 查询角色列表---不分页

     * @date 2019/9/2
     * @return List
     */
    List<AuthRolePageQueryResp> queryAllRoles();

    /**
     * 询角色对应的权限菜单

     * @date 2019/9/4
     * @param authRoleId 角色ID
     * @return List
     */
    List<String> queryMenuIdList(String authRoleId);

    /**
     * 更新信息
     *
     * @date 2021/3/30
     * @param updateReq 更新请求参数
     */
    void updateItem(AuthRoleUpdateReq updateReq);

    /**
     * 删除角色

     * @date 2019/9/4
     * @param authRoleIdList
     */
    void batchDeleteItem(String authRoleIdList);

    /**
     * 判断角色代码是否存在
     *
     * @date 2021/5/21
     * @param roleCode 角色代码
     * @return Boolean
     */
    Boolean roleCodeExisted(String roleCode);

    /**
     * 判断角色名称是否存在
     *
     * @date 2021/5/21
     * @param roleName 角色名称
     * @return Boolean
     */
    Boolean roleNameExisted(String roleName);

    /**
     * 校验角色代码的唯一性
     *
     * @date 2021/4/27
     * @param roleCode 角色代码
     */
    void verifyRoleCode(String roleCode);


    /**
      * 新增角色
      *
      * @date 2021/3/30
      * @param addReq 新增请求参数
      */
    void addItem(AuthRoleAddReq addReq);
}
