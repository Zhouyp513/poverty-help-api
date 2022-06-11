package cn.poverty.api.controller;

import cn.poverty.common.anon.ApiLog;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.interaction.req.role.AuthRoleAddReq;
import cn.poverty.interaction.req.role.AuthRolePageReq;
import cn.poverty.interaction.req.role.AuthRoleUpdateReq;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.interaction.resp.role.AuthRolePageQueryResp;
import cn.poverty.service.AuthRoleService;
import cn.poverty.service.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**

 * @projectName poverty-help-api
 * @Description: 系统角色前端控制器
 * @date 2019-08-23
 */
@RestController
@RequestMapping(value = "api/v1/authRole")
@Slf4j
public class AutoRoleController extends BaseApiController {


    @Resource
    private AuthUserService authUserService;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private AuthRoleService authRoleService;


    /**
     * 分页查询用户角色
     *
     * @date 2019-08-23
     * @param authRolePageReq 分页查询用户角色参数
     * @return Pagination
     */
    @PostMapping(value = "/queryAuthRoleByPage")
    @ApiLog(value = "分页查询用户角色")
    public ApiResponse<Pagination<AuthRolePageQueryResp>> queryAuthRoleByPage(@RequestBody(required = false)
                                                                                      AuthRolePageReq authRolePageReq){
        return apiResponse(authRoleService.queryAuthRoleByPage(authRolePageReq));
    }

    /**
     * 查询角色列表->不分页
     
     * @date 2019/9/2
     * @return ApiResponse
     */
    @GetMapping("/queryAllRoles")
    @ApiLog(value = "查询角色列表->不分页")
    public ApiResponse<List<AuthRolePageQueryResp>> queryAllRoles(){
        return  apiResponse(authRoleService.queryAllRoles());
    }

    /**
     * 根据角色ID查询角色对应的菜单权限信息
     *
     * @date 2021/5/21
     * @param authRoleId 角色ID
     * @return List
     */
    @GetMapping("/queryMenuIdList")
    @ApiLog(value = "查询角色对应的权限菜单")
    public ApiResponse<List<String>> queryMenuIdList(@NotBlank(message = "{required}") @RequestParam(name = "authRoleId") String authRoleId){
        return apiResponse(authRoleService.queryMenuIdList(authRoleId));
    }

    /**
     * 更新信息
     *
     * @date 2021/3/30
     * @param updateReq 更新请求参数
     */
    @PostMapping("/updateItem")
    @ApiLog(value = "修改角色信息")
    public ApiResponse updateAuthRole(@RequestBody @Valid AuthRoleUpdateReq updateReq){
        authRoleService.updateItem(updateReq);
        return apiResponse();
    }

    /**
      * 删除角色
      
      * @date 2019/9/4
      * @param mainIdList
      * @return
      */
    @DeleteMapping("/batchDeleteItem/{mainIdList}")
    @RequiresPermissions("dict:delete")
    @ApiLog(value = "删除角色")
    public ApiResponse batchDeleteItem(@PathVariable("mainIdList")String mainIdList){
        authRoleService.batchDeleteItem(mainIdList);
        return apiResponse();
    }

    /**
     * 判断角色代码是否存在
     *
     * @date 2021/5/21
     * @param roleCode 角色代码
     * @return Boolean
     */
    @GetMapping("/roleCodeExisted")
    @ApiLog(value = "判断角色代码是否存在")
    public ApiResponse<Boolean> roleCodeExisted(@RequestParam(name = "roleCode") String roleCode){
        return apiResponse(authRoleService.roleCodeExisted(roleCode));
    }

    /**
     * 判断角色名称是否存在
     *
     * @date 2021/5/21
     * @param roleName 角色名称
     * @return Boolean
     */
    @GetMapping("/roleNameExisted")
    @ApiLog(value = "判断角色名称是否存在")
    public ApiResponse<Boolean> roleNameExisted(@RequestParam(name = "roleName") String roleName){
        return apiResponse(authRoleService.roleNameExisted(roleName));
    }

    /**
     * 新增角色
     *
     * @date 2021/3/30
     * @param addReq 新增请求参数
     */
    @PostMapping(value = "/addItem")
    @RequiresPermissions("role:add")
    @ApiLog(value = "新增角色")
    public ApiResponse addMenu(@RequestBody @Valid AuthRoleAddReq addReq){
        authRoleService.addItem(addReq);
        return apiResponse();
    }
}
