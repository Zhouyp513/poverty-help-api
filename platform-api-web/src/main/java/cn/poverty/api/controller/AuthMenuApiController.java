package cn.poverty.api.controller;


import cn.poverty.common.anon.ApiLog;
import cn.poverty.common.entity.VueRouter;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.interaction.req.menu.AuthMenuAddReq;
import cn.poverty.interaction.req.menu.AuthMenuTreeQueryReq;
import cn.poverty.interaction.req.menu.AuthMenuUpdateReq;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.menu.AuthMenuTreeQueryWrapResp;
import cn.poverty.repository.result.AuthMenuQueryResult;
import cn.poverty.service.AuthMenuService;
import cn.poverty.service.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 
 * @projectName poverty-help-api
 * @Description: 权限菜单前端控制器
 * @date 2019-08-22
 */
@RestController
@RequestMapping(value = "api/v1/authMenu")
@Slf4j
public class AuthMenuApiController extends BaseApiController {

    @Resource
    private AuthUserService authUserService;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private AuthMenuService authMenuService;

    @Resource
    private JedisPoolConfig jedisPoolConfig;

    /**
     * 更新菜单
     * 
     * @date 2019-09-05
     * @param authMenuUpdateReq 更新菜单Req
     * @return
     */
    @PostMapping(value = "/updateMenu")
    @RequiresPermissions("menu:update")
    @ApiLog(value = "更新菜单")
    public ApiResponse updateMenu(@RequestBody AuthMenuUpdateReq authMenuUpdateReq){
        authMenuService.updateMenu(authMenuUpdateReq);
        return apiResponse();
    }

    /**
     * 新增菜单
     * 
     * @date 2019-09-05
     * @param authMenuAddReq 新增菜单请求参数
     */
    @PostMapping(value = "/addMenu")
    @RequiresPermissions("menu:add")
    @ApiLog(value = "新增菜单")
    public ApiResponse addMenu(@RequestBody AuthMenuAddReq authMenuAddReq){
        authMenuService.addMenu(authMenuAddReq);
        return apiResponse();
    }

    /**
     * 删除菜单
     * 
     * @date 2019-09-05
     * @param authMenuIdList 需要被删除的菜单的主键ID
     */
    @DeleteMapping("deleteMenu/{authMenuIdList}")
    @RequiresPermissions("menu:delete")
    @ApiLog(value = "删除菜单")
    public ApiResponse deleteMenu(@PathVariable(name= "authMenuIdList") String authMenuIdList){
        authMenuService.deleteMenu(Arrays.asList(authMenuIdList.split(",")));
        return apiResponse();
    }


    /**
     * 根据用户名查询用户能访问的菜单
     * 
     * @date 2019-08-22
     * @param userName 用户名称
     * @return java.util.List
     */
    @GetMapping("/queryVUERouterByUserName/{userName}")
    @ApiLog(value = "根据用户名查询用户能访问的菜单")
    public ApiResponse<List<VueRouter<AuthMenuQueryResult>>> queryVueRouterByUserName(@PathVariable(name = "userName") String userName){
            return apiResponse(authUserService.queryVueRouterByUserName(userName));
    }

    /**
     * 权限菜单树集合查询
     * 
     * @date 2019-08-26
     * @param authMenuTreeQueryReq 权限菜单树查询Req
     * @return AuthMenuTreeListQueryWrapResp
     */
    /*@PostMapping("/queryMenuTreeList")
    @ApiLog(value = "权限菜单树集合查询")
    public ApiResponse<AuthMenuTreeListQueryWrapResp> queryMenuTreeList(@RequestBody
                                                                                AuthMenuTreeQueryReq authMenuTreeQueryReq){
        log.info(">>>>>>>>>>>>>>>>>>查询角色权限菜单参数:{}<<<<<<<<<<<<<<<",authMenuTreeQueryReq.toString());
        return apiResponse(authMenuService.queryMenuTreeList(authMenuTreeQueryReq));
    }*/

    /**
     * 查询所有部门树集合
     * 
     * @date 2019-08-26
     * @param authMenuTreeQueryReq
     * @return AuthMenuTreeQueryWrapResp
     */
    @PostMapping("/queryAllAuthMenuTreeList")
    @ApiLog(value = "查询所有部门树集合")
    public ApiResponse<AuthMenuTreeQueryWrapResp> queryAllAuthMenuTreeList(@RequestBody
                                                                                   AuthMenuTreeQueryReq authMenuTreeQueryReq){
        return apiResponse(authMenuService.queryAllAuthMenuTreeList(authMenuTreeQueryReq));
    }

    /**
     * 查询部门树
     * 
     * @date 2019-08-26
     * @param authMenuTreeQueryReq
     * @return AuthMenuTreeQueryWrapResp
     */
    @PostMapping("/queryAllAuthMenuTree")
    @ApiLog(value = "查询部门树")
    public ApiResponse<AuthMenuTreeQueryWrapResp> queryAllAuthMenuTree(@RequestBody AuthMenuTreeQueryReq authMenuTreeQueryReq){
        return apiResponse(authMenuService.queryAllAuthMenuTree(authMenuTreeQueryReq));
    }

}

