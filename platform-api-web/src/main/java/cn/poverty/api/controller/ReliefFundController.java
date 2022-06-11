package cn.poverty.api.controller;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.interaction.req.fund.ReliefFundAddReq;
import cn.poverty.interaction.req.fund.ReliefFundPageReq;
import cn.poverty.interaction.req.fund.ReliefFundUpdateReq;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.fund.ReliefFundResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.service.ReliefFundService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
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

/**
 
 * @packageName cn.poverty.api.controller
 * @Description: 税收前端控制器
 * @date 2021-04-23
 */
@RestController
@RequestMapping(value = "api/v1/reliefFund")
@Slf4j
public class ReliefFundController extends BaseApiController {

    @Resource
    private ReliefFundService reliefFundService;

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
    @RequiresPermissions("reliefFund:add")
    public ApiResponse addItem(@RequestBody @Valid ReliefFundAddReq addReq){
        reliefFundService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量
     * 
     * @date 2021/2/2
     * @param mainIdList 主键ID集合
     */
    @DeleteMapping(value = "/batchDeleteItem/{mainIdList}")
    @RequiresPermissions("reliefFund:batchDelete")
    public ApiResponse batchDeleteItem(@PathVariable(name = "mainIdList") String mainIdList){
        reliefFundService.batchDeleteItem(Arrays.asList(mainIdList.split(",")));
        return apiResponse();
    }


    /**
     * 扶贫救助金查询
     * 
     * @date 2021/2/15
     * @param  pageReq 扶贫救助金查询Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @RequiresPermissions("reliefFund:itemList")
    public ApiResponse<Pagination<ReliefFundResp>> queryByPage(
            @RequestBody @Valid ReliefFundPageReq pageReq){
        return apiResponse(reliefFundService.queryByPage(pageReq));
    }

    /**
     * 更新
     * 
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    @PutMapping(value = "/updateItem")
    @RequiresPermissions("reliefFund:edit")
    public ApiResponse updateItem(@RequestBody @Valid ReliefFundUpdateReq updateReq){
        reliefFundService.updateItem(updateReq);
        return apiResponse();
    }
}
