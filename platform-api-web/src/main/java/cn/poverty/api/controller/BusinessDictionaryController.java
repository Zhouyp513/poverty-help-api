package cn.poverty.api.controller;


import cn.poverty.common.anon.ApiLog;
import cn.poverty.interaction.req.dict.BusinessDictAddReq;
import cn.poverty.interaction.req.dict.BusinessDictPageReq;
import cn.poverty.interaction.req.dict.BusinessDictUpdateReq;
import cn.poverty.interaction.resp.base.ApiResponse;
import cn.poverty.interaction.resp.dict.BusinessDictionaryResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.service.BusinessDictionaryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 
 * @projectName poverty-help-api
 * @Description: 系统字典前端控制器
 * @date 2019-08-28
 */
@RestController
@RequestMapping("api/v1/businessDictionary")
public class BusinessDictionaryController extends BaseApiController {


    @Resource
    private BusinessDictionaryService businessDictionaryService;


    /**
     * 分页查询系统业务字典信息
     * 
     * @date 2020-07-07
     * @param pageReq 分页查询系统业务字典Req
     * @return ApiResponse
     */
    @PostMapping("/queryByPage")
    @RequiresPermissions("businessDictionary:itemList")
    @ApiLog(value = "分页查询系统业务字典信息")
    public ApiResponse<Pagination<BusinessDictionaryResp>> queryByPage(@RequestBody @Valid
                                                                               BusinessDictPageReq pageReq){
        return apiResponse(businessDictionaryService.queryByPage(pageReq));
    }

    /**
     * 新增系统字典参数
     * 
     * @date 2020-07-07
     * @param addReq 新增系统字典参数请求Req
     * @return ApiResponse
     */
    @PostMapping("/addItem")
    @RequiresPermissions("businessDictionary:addItem")
    @ApiLog(value = "新增系统字典参数")
    public ApiResponse addItem(@RequestBody @Valid BusinessDictAddReq addReq){
        businessDictionaryService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 系统字典更新
     * 
     * @date 2020-07-07
     * @param updateReq 系统字典更新Req
     * @return ApiResponse
     */
    @PostMapping("/updateItem")
    @RequiresPermissions("businessDictionary:updateItem")
    @ApiLog(value = "系统字典更新")
    public ApiResponse updateItem(@RequestBody @Valid BusinessDictUpdateReq updateReq){
        businessDictionaryService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 根据系统业务字典主键ID系统字典信息
     * 
     * @date 2020-07-07
     * @param mainIdList 系统字典主键ID集合
     * @return ApiResponse
     */
    @DeleteMapping("/batchDeleteItem/{mainIdList}")
    @RequiresPermissions("businessDictionary:batchDelete")
    @ApiLog(value = "根据系统业务字典主键ID系统字典信息")
    public ApiResponse deleteByMainId(@PathVariable(value = "mainIdList") String mainIdList){
        businessDictionaryService.deleteByMainId(Arrays.asList(mainIdList.split(",")));
        return apiResponse();
    }

    /**
     * 根据字典分类查询字典信息
     * 
     * @date 2021/1/21
     * @param dictType 字段类型
     * @return List
     */
    @GetMapping(value = "/testQueryDictionaryByType")
    public ApiResponse<String> testQueryDictionaryByType(@RequestParam(name = "dictType") String dictType){
        return apiResponse("123");
    }

    /**
     * 根据字典分类查询字典信息
     * 
     * @date 2021/1/21
     * @param dictType 字段类型
     * @return List
     */
    @GetMapping(value = "/queryDictionaryByType")
    public ApiResponse<List<BusinessDictionaryResp>> queryDictionaryByType(@RequestParam(name = "dictType") String dictType){
        return apiResponse(businessDictionaryService.queryDictionaryByType(dictType));
    }

    /**
     * 根据字典分类查询单个字典信息
     * 
     * @date 2021/1/21
     * @param dictType 字段类型
     * @return cn.poverty.interaction.resp.dict.BusinessDictionaryResp
     */
    @GetMapping(value = "/querySingleDictByType")
    public ApiResponse<BusinessDictionaryResp> querySingleDictByType(@RequestParam(name = "dictType") String dictType){
        return apiResponse(businessDictionaryService.querySingleDictByType(dictType));
    }

    /**
     * 根据字典值模糊查询字典信息
     * 
     * @date 2021/1/21
     * @param dictValue 字段值
     * @return List
     */
    @GetMapping(value = "/queryDictionaryByValue")
    public ApiResponse<List<BusinessDictionaryResp>> queryDictionaryByValue(@RequestParam(name = "dictValue") String dictValue){
        return apiResponse(businessDictionaryService.queryDictionaryByValue(dictValue));
    }

}
