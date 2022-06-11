package cn.poverty.api.controller;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.interaction.req.item.ReliefItemAddReq;
import cn.poverty.interaction.req.item.ReliefItemPageReq;
import cn.poverty.interaction.req.item.ReliefItemUpdateReq;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.item.ReliefItemResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.service.ReliefItemService;
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
@RequestMapping(value = "api/v1/reliefItem")
@Slf4j
public class ReliefItemController extends BaseApiController {

    @Resource
    private ReliefItemService reliefItemService;

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
    @RequiresPermissions("reliefItem:add")
    public ApiResponse addItem(@RequestBody @Valid ReliefItemAddReq addReq){
        reliefItemService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量
     *
     * @date 2021/2/2
     * @param mainIdList 主键ID集合
     */
    @DeleteMapping(value = "/batchDeleteItem/{mainIdList}")
    @RequiresPermissions("reliefItem:batchDelete")
    public ApiResponse batchDeleteItem(@PathVariable(name = "mainIdList") String mainIdList){
        reliefItemService.batchDeleteItem(Arrays.asList(mainIdList.split(",")));
        return apiResponse();
    }


    /**
     * 分页查询
     *
     * @date 2021/2/15
     * @param  pageReq 分页查询Req  查询救助金信息
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @RequiresPermissions("reliefItem:itemList")
    public ApiResponse<Pagination<ReliefItemResp>> queryByPage(
            @RequestBody @Valid ReliefItemPageReq pageReq){
        return apiResponse(reliefItemService.queryByPage(pageReq));
    }

    /**
     * 查询所有项目
     *
     * @date 2021/10/25
     * @return java.util.List
     */
    @GetMapping(value = "/queryAllItem")
    public ApiResponse<List<ReliefItemResp>> queryAllItem(){
        return apiResponse(reliefItemService.queryAllItem());
    }

    /**
     * 更新
     *
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    @PutMapping(value = "/updateItem")
    @RequiresPermissions("reliefItem:edit")
    public ApiResponse updateItem(@RequestBody @Valid ReliefItemUpdateReq updateReq){
        reliefItemService.updateItem(updateReq);
        return apiResponse();
    }
}
