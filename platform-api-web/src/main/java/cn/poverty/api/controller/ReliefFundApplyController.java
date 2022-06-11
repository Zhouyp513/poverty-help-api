package cn.poverty.api.controller;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.interaction.req.fund.apply.FundApplyReq;
import cn.poverty.interaction.req.fund.apply.ReliefFundApplyAddReq;
import cn.poverty.interaction.req.fund.apply.ReliefFundApplyPageReq;
import cn.poverty.interaction.req.fund.apply.ReliefFundApplyUpdateReq;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.fund.apply.ReliefFundApplyResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.service.ReliefFundApplyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping(value = "api/v1/reliefFundApply")
@Slf4j
public class ReliefFundApplyController extends BaseApiController {

    @Resource
    private ReliefFundApplyService reliefFundApplyService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private IExcelDictHandler excelDictHandler;


    /**
     * 申请救助金
     * @author
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    @PostMapping(value = "/applyFund")
    @RequiresPermissions("reliefFundApply:add")
    public ApiResponse applyFund(@RequestBody @Valid FundApplyReq addReq){
        reliefFundApplyService.applyFund(addReq);
        return apiResponse();
    }

    /**
     * 新增
     * @author
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    @PostMapping(value = "/addItem")
    @RequiresPermissions("reliefFundApply:add")
    public ApiResponse addItem(@RequestBody @Valid ReliefFundApplyAddReq addReq){
        reliefFundApplyService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量
     *
     * @date 2021/2/2
     * @param mainIdList 主键ID集合
     */
    @DeleteMapping(value = "/batchDeleteItem/{mainIdList}")
    @RequiresPermissions("reliefFundApply:batchDelete")
    public ApiResponse batchDeleteItem(@PathVariable(name = "mainIdList") String mainIdList){
        reliefFundApplyService.batchDeleteItem(Arrays.asList(mainIdList.split(",")));
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
    @RequiresPermissions("reliefFundApply:itemList")
    public ApiResponse<Pagination<ReliefFundApplyResp>> queryByPage(
            @RequestBody @Valid ReliefFundApplyPageReq pageReq){
        return apiResponse(reliefFundApplyService.queryByPage(pageReq));
    }

    /**
     * 更新
     *
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    @PutMapping(value = "/updateItem")
    @RequiresPermissions("reliefFundApply:edit")
    public ApiResponse updateItem(@RequestBody @Valid ReliefFundApplyUpdateReq updateReq){
        reliefFundApplyService.updateItem(updateReq);
        return apiResponse();
    }
}
