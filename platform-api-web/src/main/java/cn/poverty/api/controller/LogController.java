package cn.poverty.api.controller;

import cn.poverty.common.anon.ApiLog;
import cn.poverty.interaction.req.log.SystemApiLogPageReq;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.log.SystemApiLogPageQueryResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**

 * @projectName poverty-help-api
 * @Description: 日志前端控制器
 * @date 2020-01-13
 */
@RestController
@RequestMapping(value = "api/v1/apiLog")
@Slf4j
public class LogController extends BaseApiController {

    @Resource
    private LogService logService;




    /**
     * 分页查询系统日志
     *
     * @date 2020-01-10
     * @param systemApiLogPageReq 系统API日志查询请求Req
     * @return ApiResponse
     */
    @PostMapping("/querySystemApiLogByPage")
    @RequiresPermissions("log:queryOperateLog")
    @ApiLog(value = "分页查询系统日志")
    public ApiResponse<Pagination<SystemApiLogPageQueryResp>> querySystemApiLogByPage(@RequestBody @Valid
                                                                                              SystemApiLogPageReq systemApiLogPageReq){
        return apiResponse(logService.querySystemApiLogByPage(systemApiLogPageReq));
    }
}
