package cn.poverty.api.controller;

import cn.poverty.common.anon.ApiLog;
import cn.poverty.common.entity.Tree;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.interaction.req.department.DepartmentAddReq;
import cn.poverty.interaction.req.department.DepartmentQueryReq;
import cn.poverty.interaction.req.department.DepartmentUpdateReq;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.department.AuthDepartmentTreeResp;
import cn.poverty.interaction.resp.department.AuthDepartmentTreeWrapResp;
import cn.poverty.service.AuthDepartmentService;
import cn.poverty.service.AuthUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;

/**
 
 * @projectName poverty-help-api
 * @Description: 部门前端控制器
 * @date 2019-08-23
 */
@RestController
@RequestMapping(value = "api/v1/authDepartment")
public class AuthDepartmentController extends BaseApiController {


    @Resource
    private AuthUserService authUserService;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private AuthDepartmentService authDepartmentService;


    /**
     * 新增部门
     * 
     * @date 2019-08-30
     * @param departmentAddReq 新增部门
     */
    @PostMapping(value = "/addAuthDepartment")
    @RequiresPermissions("authDepartment:add")
    @ApiLog(value = "新增部门")
    public ApiResponse addAuthDepartment(@RequestBody DepartmentAddReq departmentAddReq){
        authDepartmentService.addAuthDepartment(departmentAddReq);
        return apiResponse();
    }

    /**
     * 更新部门信息
     * 
     * @date 2019-08-30
     * @param departmentUpdateReq 部门更新req
     * @return
     */
    @PutMapping(value = "/updateDepartment")
    @RequiresPermissions("authDepartment:updateItem")
    @ApiLog(value = "更新部门信息")
    public ApiResponse updateDepartment(@RequestBody DepartmentUpdateReq departmentUpdateReq){
        authDepartmentService.updateDepartment(departmentUpdateReq);
        return apiResponse();
    }

    /**
     * 删除部门
     * 
     * @date 2019-09-05
     * @param departmentIdList 需要被删除的部门的主键ID
     */
    @DeleteMapping("/deleteDepartment/{departmentIdList}")
    @RequiresPermissions("updateDepartment:delete")
    @ApiLog(value = "删除部门")
    public ApiResponse deleteDepartment(@PathVariable(name= "departmentIdList") String departmentIdList){
        authDepartmentService.deleteDepartment(Arrays.asList(departmentIdList.split(",")));
        return apiResponse();
    }

    /**
     * 查询所有的部门的树型结构
     * 
     * @date 2019-08-23
     * @return Tree
     */
    @PostMapping(value = "/queryAllDepartmentTreeList")
    @RequiresPermissions("authDepartment:itemList")
    @ApiLog(value = "查询所有的部门的树型结构")
    public ApiResponse<AuthDepartmentTreeWrapResp> queryAllDepartmentTreeList(
            @RequestBody(required = false) @Valid DepartmentQueryReq queryReq){
        return apiResponse(authDepartmentService.queryAllDepartmentTreeList(queryReq));
    }


    /**
     * 查询所有的部门的树型结构
     * 
     * @date 2019-08-23
     * @return Tree
     */
    @GetMapping(value = "/queryAllDepartmentTree")
    @RequiresPermissions("authDepartment:itemTreeList")
    @ApiLog(value = "查询所有的部门的树型结构")
    public ApiResponse<Tree<AuthDepartmentTreeResp>> queryAllDepartmentTree(){
        return apiResponse(authDepartmentService.queryAllDepartmentTree());
    }
}
