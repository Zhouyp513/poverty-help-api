package cn.poverty.service;

import cn.poverty.common.entity.Tree;
import cn.poverty.interaction.req.department.DepartmentAddReq;
import cn.poverty.interaction.req.department.DepartmentQueryReq;
import cn.poverty.interaction.req.department.DepartmentUpdateReq;
import cn.poverty.interaction.resp.department.AuthDepartmentTreeResp;
import cn.poverty.interaction.resp.department.AuthDepartmentTreeWrapResp;

import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 部门Service
 * @date 2019-08-23
 */
public interface AuthDepartmentService  {

    /**
     * 新增部门
     *
     * @date 2019-08-30
     * @param departmentAddReq 新增部门
     */
    void addAuthDepartment(DepartmentAddReq departmentAddReq);


    /**
     * 查询所有的部门的树型结构
     *
     * @date 2019-08-23
     * @param departmentQueryReq 部门分页查询req
     * @return AuthDepartmentTreeWrapResp
     */
    AuthDepartmentTreeWrapResp queryAllDepartmentTreeList(DepartmentQueryReq departmentQueryReq);


    /**
     * 查询所有的部门的树型结构
     *
     * @date 2019-08-23
     * @return Tree
     */
    Tree<AuthDepartmentTreeResp> queryAllDepartmentTree();

    /**
     * 更新部门信息
     *
     * @date 2019-08-30
     * @param departmentUpdateReq 部门更新req
     */
    void updateDepartment(DepartmentUpdateReq departmentUpdateReq);

    /**
     * 删除部门
     *
     * @date 2019-09-05
     * @param departmentIdList 需要被删除的部门的主键ID
     */
    void deleteDepartment(List<String> departmentIdList);

}
