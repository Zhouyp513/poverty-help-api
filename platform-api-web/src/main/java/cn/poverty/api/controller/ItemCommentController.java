package cn.poverty.api.controller;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.interaction.req.item.comment.ItemCommentAddReq;
import cn.poverty.interaction.req.item.comment.ItemCommentPageReq;
import cn.poverty.interaction.req.item.comment.ItemCommentUpdateReq;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.item.comment.ItemCommentResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.service.ItemCommentService;
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
@RequestMapping(value = "api/v1/itemComment")
@Slf4j
public class ItemCommentController extends BaseApiController {

    @Resource
    private ItemCommentService itemCommentService;

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
    @RequiresPermissions("itemComment:add")
    public ApiResponse addItem(@RequestBody @Valid ItemCommentAddReq addReq){
        itemCommentService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量
     * 
     * @date 2021/2/2
     * @param mainIdList 主键ID集合
     */
    @DeleteMapping(value = "/batchDeleteItem/{mainIdList}")
    @RequiresPermissions("itemComment:batchDelete")
    public ApiResponse batchDeleteItem(@PathVariable(name = "mainIdList") String mainIdList){
        itemCommentService.batchDeleteItem(Arrays.asList(mainIdList.split(",")));
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
    @RequiresPermissions("itemComment:itemList")
    public ApiResponse<Pagination<ItemCommentResp>> queryByPage(
            @RequestBody @Valid ItemCommentPageReq pageReq){
        return apiResponse(itemCommentService.queryByPage(pageReq));
    }

    /**
     * 更新
     * 
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    @PutMapping(value = "/updateItem")
    @RequiresPermissions("itemComment:edit")
    public ApiResponse updateItem(@RequestBody @Valid ItemCommentUpdateReq updateReq){
        itemCommentService.updateItem(updateReq);
        return apiResponse();
    }
}
