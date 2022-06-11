package cn.poverty.api.controller;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.interaction.req.archive.PovertyArchiveAddReq;
import cn.poverty.interaction.req.archive.PovertyArchivePageReq;
import cn.poverty.interaction.req.archive.PovertyArchiveUpdateReq;
import cn.poverty.interaction.resp.archive.PovertyArchiveResp;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.service.PovertyArchiveService;
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
 
 * @packageName cn.poverty.api.controller
 * @Description: 税收前端控制器
 * @date 2021-04-23
 */
@RestController
@RequestMapping(value = "api/v1/povertyArchive")
@Slf4j
public class PovertyArchiveController extends BaseApiController {

    @Resource
    private PovertyArchiveService povertyArchiveService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private IExcelDictHandler excelDictHandler;

    /**
     * 新增
     * 
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    @PostMapping(value = "/addItem")
    @RequiresPermissions("povertyArchive:add")
    public ApiResponse addItem(@RequestBody @Valid PovertyArchiveAddReq addReq){
        povertyArchiveService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量
     * 
     * @date 2021/2/2
     * @param mainIdList 主键ID集合
     */
    @DeleteMapping(value = "/batchDeleteItem/{mainIdList}")
    @RequiresPermissions("povertyArchive:batchDelete")
    public ApiResponse batchDeleteItem(@PathVariable(name = "mainIdList") String mainIdList){
        povertyArchiveService.batchDeleteItem(Arrays.asList(mainIdList.split(",")));
        return apiResponse();
    }


    /**
     * 分页查询
     * 
     * @date 2021/2/15
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @RequiresPermissions("povertyArchive:itemList")
    public ApiResponse<Pagination<PovertyArchiveResp>> queryByPage(
            @RequestBody @Valid PovertyArchivePageReq pageReq){
        return apiResponse(povertyArchiveService.queryByPage(pageReq));
    }

    /**
     * 查询所有项目
     * 
     * @date 2021/10/25
     * @return java.util.List
     */
    @GetMapping(value = "/queryAllItem")
    public ApiResponse<List<PovertyArchiveResp>> queryAllItem(){
        return apiResponse(povertyArchiveService.queryAllItem());
    }

    /**
     * 更新
     * 
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    @PutMapping(value = "/updateItem")
    @RequiresPermissions("povertyArchive:edit")
    public ApiResponse updateItem(@RequestBody @Valid PovertyArchiveUpdateReq updateReq){
        povertyArchiveService.updateItem(updateReq);
        return apiResponse();
    }
}
