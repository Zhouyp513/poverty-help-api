package cn.poverty.api.controller;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.interaction.req.item.apply.ReliefItemApplyAddReq;
import cn.poverty.interaction.req.item.apply.ReliefItemApplyPageReq;
import cn.poverty.interaction.req.item.apply.ReliefItemApplyReq;
import cn.poverty.interaction.req.item.apply.ReliefItemApplyUpdateReq;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.item.apply.ReliefItemApplyResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.service.ReliefItemApplyService;
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
@RequestMapping(value = "api/v1/reliefItemApply")
@Slf4j
public class ReliefItemApplyController extends BaseApiController {

    @Resource
    private ReliefItemApplyService reliefItemApplyService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private IExcelDictHandler excelDictHandler;


    /**
     * 申请扶贫项目
     * @author
     * @date 2021/2/15
     * @param addReq 申请扶贫项目请求参数
     */
    @PostMapping(value = "/applyItem")
    @RequiresPermissions("reliefItemApply:add")
    public ApiResponse applyItem(@RequestBody @Valid ReliefItemApplyReq addReq){
        reliefItemApplyService.applyItem(addReq);
        return apiResponse();
    }

    /**
     * 新增
     * @author
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    @PostMapping(value = "/addItem")
    @RequiresPermissions("reliefItemApply:add")
    public ApiResponse addItem(@RequestBody @Valid ReliefItemApplyAddReq addReq){
        reliefItemApplyService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量
     *
     * @date 2021/2/2
     * @param mainIdList 主键ID集合
     */
    @DeleteMapping(value = "/batchDeleteItem/{mainIdList}")
    @RequiresPermissions("reliefItemApply:batchDelete")
    public ApiResponse batchDeleteItem(@PathVariable(name = "mainIdList") String mainIdList){
        reliefItemApplyService.batchDeleteItem(Arrays.asList(mainIdList.split(",")));
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
    @RequiresPermissions("reliefItemApply:itemList")
    public ApiResponse<Pagination<ReliefItemApplyResp>> queryByPage(
            @RequestBody @Valid ReliefItemApplyPageReq pageReq){
        return apiResponse(reliefItemApplyService.queryByPage(pageReq));
    }

    /**
     * 更新
     *
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    @PutMapping(value = "/updateItem")
    @RequiresPermissions("reliefItemApply:edit")
    public ApiResponse updateItem(@RequestBody @Valid ReliefItemApplyUpdateReq updateReq){
        reliefItemApplyService.updateItem(updateReq);
        return apiResponse();
    }
}
