package cn.poverty.service;

import cn.poverty.interaction.req.dict.BusinessDictAddReq;
import cn.poverty.interaction.req.dict.BusinessDictPageReq;
import cn.poverty.interaction.req.dict.BusinessDictUpdateReq;
import cn.poverty.interaction.resp.dict.BusinessDictionaryResp;
import cn.poverty.interaction.resp.page.Pagination;

import java.util.List;

/**
 
 * @projectName poverty-help-api
 * @Description: 系统字典Service
 * @date 2019-08-28
 */
public interface BusinessDictionaryService {


    /**
     * 根据系统业务字典主键ID系统字典信息
     * 
     * @date 2020-07-07
     * @param businessDictionaryIdList 系统字典主键ID集合
     */
    void deleteByMainId(List<String> businessDictionaryIdList);

    /**
     * 系统字典更新
     * 
     * @date 2020-07-07
     * @param businessDictUpdateReq 系统字典更新Req
     */
    void updateItem(BusinessDictUpdateReq businessDictUpdateReq);

    /**
     * 分页查询系统业务字典信息
     * 
     * @date 2020-07-07
     * @param businessDictPageReq 分页查询系统业务字典Req
     * @return Pagination
     */
    Pagination<BusinessDictionaryResp> queryByPage(
            BusinessDictPageReq businessDictPageReq);

    /**
     * 新增系统字典参数
     * 
     * @date 2020-07-07
     * @param businessDictAddReq 新增系统字典参数请求Req
     * @return
     */
    void addItem(BusinessDictAddReq businessDictAddReq);

    /**
     * 根据字典值模糊查询字典信息
     * 
     * @date 2021/1/21
     * @param dictValue 字段值
     * @return List
     */
    List<BusinessDictionaryResp> queryDictionaryByValue(String dictValue);

    /**
     * 根据字典分类查询单个字典信息
     * 
     * @date 2021/1/21
     * @param dictType 字段类型
     * @return cn.poverty.interaction.resp.dict.BusinessDictionaryResp
     */
    BusinessDictionaryResp querySingleDictByType(String dictType);

    /**
     * 根据字典分类查询字典信息
     * 
     * @date 2021/1/21
     * @param dictType 字段类型
     * @return List
     */
    List<BusinessDictionaryResp> queryDictionaryByType(String dictType);


}
