package cn.poverty.api.controller;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.interaction.req.trace.TraceRecordAddReq;
import cn.poverty.interaction.req.trace.TraceRecordPageReq;
import cn.poverty.interaction.req.trace.TraceRecordUpdateReq;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.interaction.resp.trace.TraceRecordResp;
import cn.poverty.service.TraceRecordService;
import lombok.extern.slf4j.Slf4j;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author
 * @packageName cn.poverty.api.controller
 * @Description: 税收前端控制器
 * @date 2021-04-23
 */
@RestController
@RequestMapping(value = "api/v1/traceRecord")
@Slf4j
public class TraceRecordController extends BaseApiController {

    @Resource
    private TraceRecordService traceRecordService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private IExcelDictHandler excelDictHandler;

    /**
     * 新增
     * @author
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    @PostMapping(value = "/addItem")
    @RequiresPermissions("traceRecord:add")
    public ApiResponse addItem(@RequestBody @Valid TraceRecordAddReq addReq){
        traceRecordService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量
     * @author
     * @date 2021/2/2
     * @param mainIdList 主键ID集合
     */
    @DeleteMapping(value = "/batchDeleteItem/{mainIdList}")
    @RequiresPermissions("traceRecord:batchDelete")
    public ApiResponse batchDeleteItem(@PathVariable(name = "mainIdList") String mainIdList){
        traceRecordService.batchDeleteItem(Arrays.asList(mainIdList.split(",")));
        return apiResponse();
    }


    /**
     * 分页查询
     * @author
     * @date 2021/2/15
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @RequiresPermissions("traceRecord:itemList")
    public ApiResponse<Pagination<TraceRecordResp>> queryByPage(
            @RequestBody @Valid TraceRecordPageReq pageReq){
        return apiResponse(traceRecordService.queryByPage(pageReq));
    }

    /**
     * 查询所有项目
     * @author
     * @date 2021/10/25
     * @return java.util.List
     */
    @GetMapping(value = "/queryAllItem")
    public ApiResponse<List<TraceRecordResp>> queryAllItem(){
        return apiResponse(traceRecordService.queryAllItem());
    }

    /**
     * 更新
     * @author
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    @PutMapping(value = "/updateItem")
    @RequiresPermissions("traceRecord:edit")
    public ApiResponse updateItem(@RequestBody @Valid TraceRecordUpdateReq updateReq){
        traceRecordService.updateItem(updateReq);
        return apiResponse();
    }
}
