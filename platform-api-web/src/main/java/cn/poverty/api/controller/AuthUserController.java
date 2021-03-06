package cn.poverty.api.controller;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.common.anon.ApiLog;
import cn.poverty.common.export.ExcelHandler;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.interaction.internal.auth.ActiveUser;
import cn.poverty.interaction.req.auth.AuthLoginReq;
import cn.poverty.interaction.req.auth.AuthUserAddReq;
import cn.poverty.interaction.req.auth.AuthUserPageReq;
import cn.poverty.interaction.req.auth.AuthUserUpdateReq;
import cn.poverty.interaction.req.user.AuthUserConfigUpdateReq;
import cn.poverty.interaction.req.user.UserProfileUpdateReq;
import cn.poverty.interaction.resp.auth.AuthorizedUserResp;
import cn.poverty.interaction.resp.auth.AuthorizedUserWrapResp;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.interaction.resp.role.AuthRoleResp;
import cn.poverty.interaction.resp.user.AuthUserExportResp;
import cn.poverty.interaction.resp.user.AuthUserPageQueryResp;
import cn.poverty.interaction.resp.user.AuthUserSketchResp;
import cn.poverty.service.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description:
 * @date 2019-08-13
 */
@RestController
@RequestMapping(value = "api/v1/authUser")
@Slf4j
public class AuthUserController extends BaseApiController {

    @Resource
    private AuthUserService authUserService;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private HttpServletResponse response;

    @Resource
    private IExcelDictHandler excelDictHandler;


    /**
     * ????????????????????????????????????
     * @author
     * @date 2019-08-21
     * @param userName ????????????
     * @return AuthorizedUserResp
     */
    @GetMapping("/{userName}")
    @ApiLog(value = "????????????????????????????????????")
    public AuthorizedUserResp queryAuthorizedUserByUserName(@PathVariable String userName){
        return authUserService.queryAuthorizedUserByUserName(userName);
    }

    /**
     * ??????????????????????????????????????????
     * @author
     * @date 2019-08-21
     * @param userName ????????????
     * @return Boolean
     */
    @GetMapping("check/{userName}")
    @ApiLog(value = "??????????????????????????????????????????")
    public Boolean judgeUserExistByUserName( @PathVariable(name = "userName") String userName){
        return authUserService.judgeUserExistByUserName(userName);
    }

    /**
     * ??????????????????
     * @author
     * @date 2019-08-21
     * @param loginReq ??????????????????
     * @return cn.poverty.interaction.resp.base.ApiResponse
     */
    @PostMapping("/authUserLogin")
    @ApiLog(value = "????????????")
    public ApiResponse<AuthorizedUserWrapResp> authUserLogin(@RequestBody @Valid
                                                                     AuthLoginReq loginReq){
        return apiResponse(authUserService.authUserLogin(loginReq));
    }

    /**
     * ??????????????????
     * @author
     * @date 2019-08-21
     * @param authUserId ???????????????ID
     */
    @DeleteMapping("/authUserLogOut/{authUserId}")
    @RequiresPermissions("user:kickout")
    @ApiLog(value = "??????????????????")
    public ApiResponse authUserLogOut(@PathVariable(name="authUserId") String authUserId){
       authUserService.authUserLogOut(authUserId);
       return apiResponse();
    }


    /**
     * ?????????????????????????????????
     * @author
     * @date 2019-08-23
     * @return List
     */
    @GetMapping("/queryAllActiveUser")
    @RequiresPermissions("user:online")
    @ApiLog(value = "????????????????????????????????????????????????")
    public ApiResponse<List<ActiveUser>> queryAllActiveUser(){
        return apiResponse(authUserService.queryAllActiveUser());
    }

    /**
      *
      * ??????????????????
      * @author
      * @date 2019-08-13
      * @param authUserPageReq ??????????????????????????????
      * @return ApiResponse
      */
    @PostMapping("/queryUserByPage")
    @RequiresPermissions("user:view")
    @ApiLog(value = "??????????????????")
    public ApiResponse<Pagination<AuthUserPageQueryResp>> queryUserByPage(@RequestBody AuthUserPageReq authUserPageReq){
        return apiResponse(authUserService.queryUserByPage(authUserPageReq));
    }

    /**
     * ??????????????????
     * @author
     * @date 2019-08-21
     * @param req ??????????????????Req
     * @return cn.poverty.interaction.resp.page.Pagination
     */
    @PostMapping(value = "/exportData")
    @RequiresPermissions("authUser:export")
    @ApiLog(value = "????????????")
    public void exportData(@RequestBody(required = false) @Valid AuthUserPageReq req){
        List<AuthUserExportResp> dataList = authUserService.exportData(req);
        ExcelHandler.exportExcel(dataList, "?????????", "?????????",
                AuthUserExportResp.class, "?????????.xls",true, response,excelDictHandler);
    }

    /**
     * ???????????? (???????????????????????????)
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2021/4/22
     * @param file ??????
     */
    @PostMapping("/importData")
    @ApiLog(value = "????????????")
    public ApiResponse importData(@RequestParam("file") MultipartFile file){
        authUserService.importData(file);
        return apiResponse();
    }

    /**
     * ??????????????????
     * @author
     * @date 2019-08-29
     * @param userConfig
     * @return
     */
    @PostMapping("/updateUserConfig")
    @ApiLog(value = "??????????????????")
    public ApiResponse updateUserConfig(@RequestBody AuthUserConfigUpdateReq userConfig){
        authUserService.updateUserConfig(userConfig);
        return apiResponse();
    }

    /**
     * ??????????????????
     * @author
     * @date 2021/4/18
     * @param userProfileUpdateReq ??????????????????????????????
     */
    @PutMapping("/updateAuthUserProfile")
    @ApiLog(value = "??????????????????")
    public ApiResponse updateAuthUserProfile(@RequestBody @Valid UserProfileUpdateReq userProfileUpdateReq){
        authUserService.updateAuthUserProfile(userProfileUpdateReq);
        return apiResponse();
    }

    /**
     * ?????????????????????????????????
     * @author
     * @date 2021/4/6
     * @return List
     */
    @GetMapping("/queryAllAuthUserSketch")
    public ApiResponse<List<AuthUserSketchResp>> queryAllAuthUserSketch(){
        return apiResponse(authUserService.queryAllAuthUserSketch());
    }

    /**
     * ????????????
     * @author
     * @date 2019/8/29
     * @param userName ?????????
     * @param password ??????
     * @return
     */
    @GetMapping("/updatePassword")
    @ApiLog(value = "????????????")
    public ApiResponse updatePassword(@Valid String userName, @Valid String password){
        authUserService.updatePassword(userName,password);
        return apiResponse();
    }

    /**
     * ???????????????
     * @author
     * @date 2019/8/30
     * @param  userName
     * @return ApiResponse
     */
    @GetMapping("/checkOldPassword")
    @ApiLog(value = "???????????????")
    public ApiResponse checkOldPassword(@Valid String userName, @Valid String password){
        authUserService.checkOldPassword(userName,password);
        return apiResponse();
    }

    /**
     * ???????????????
     * @author
     * @date 2019/8/30
     * @param  userName ?????????
     */
    @GetMapping("/checkUserName")
    @ApiLog(value = "???????????????")
    public ApiResponse checkUserName(@RequestParam(name = "userName") String userName){
        authUserService.checkUserName(userName);
        return apiResponse();
    }

    /**
     * ??????????????????
     * @author
     * @date 2019/8/31
     * @param updateReq ??????????????????????????????
     * @return ApiResponse
     */
    @PutMapping("/updateItem")
    @ApiLog(value = "??????????????????")
    public ApiResponse updateItem(@RequestBody @Valid AuthUserUpdateReq updateReq){
        authUserService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * ????????????
     * @author
     * @date 2019/8/31
     * @param addReq
     * @return ApiResponse
     */
    @PostMapping("/addAuthUser")
    @ApiLog(value = "????????????")
    public ApiResponse addAuthUser(@RequestBody AuthUserAddReq addReq){
        authUserService.addNewAuthUser(addReq);
        return apiResponse();
    }

    /**
     * ????????????
     * @author
     * @date 2021/4/6
     * @param addReq ????????????????????????
     */
    @PostMapping("/register")
    @ApiLog(value = "????????????")
    public ApiResponse register(@RequestBody AuthUserAddReq addReq){
        authUserService.register(addReq);
        return apiResponse();
    }

    /**
     * ????????????
     * @author
     * @date 2019/9/2
     * @param mainIdList ??????ID??????
     * @return ApiResponse
     */
    @DeleteMapping("/batchDeleteItem/{mainIdList}")
    @RequiresPermissions("dict:delete")
    @ApiLog(value = "????????????")
    public ApiResponse batchDeleteItem(@PathVariable("mainIdList")String mainIdList){
        authUserService.batchDeleteItem(mainIdList);
        return apiResponse();
    }

    /**
     * ????????????
     * @author
     * @date 2019/9/2
     * @param userNameList ???????????????
     * @return
     */
    @GetMapping("/resetPassword")
    @ApiLog(value = "??????????????????")
    public ApiResponse resetPassword(@RequestParam("userNameList") String userNameList){
        authUserService.resetPassword(userNameList);
        return apiResponse();
    }

    /**
     * ??????????????????
     * @author
     * @date 2021/4/6
     * @return List
     */
    @GetMapping("/queryAllAuthRole")
    @ApiLog(value = "??????????????????")
    public ApiResponse<List<AuthRoleResp>> queryAllAuthRole(){
        return apiResponse(authUserService.queryAllAuthRole());
    }
}
