package cn.poverty.service.impl;

import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.utils.CriteriaBuilder;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.interaction.helper.PageBuilder;
import cn.poverty.interaction.req.dict.BusinessDictAddReq;
import cn.poverty.interaction.req.dict.BusinessDictPageReq;
import cn.poverty.interaction.req.dict.BusinessDictUpdateReq;
import cn.poverty.interaction.resp.dict.BusinessDictionaryResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.repository.entity.BusinessDictionary;
import cn.poverty.repository.repository.BusinessDictionaryRepository;
import cn.poverty.service.BusinessDictionaryService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统字典Service
 * @date 2019-08-28
 */
@Service(value = "businessDictionaryService")
@Slf4j
public class BusinessDictionaryServiceImpl implements BusinessDictionaryService {

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private BusinessDictionaryRepository businessDictionaryRepository;

    @Resource
    private MapperFacade mapperFacade;



    /**
     * 根据系统业务字典主键ID系统字典信息
     *
     * @date 2020-07-07
     * @param businessDictionaryIdList 系统字典主键ID集合
     * @return
     */
    @Override
    public void deleteByMainId(List<String> businessDictionaryIdList){
        log.info(">>>>>>>>>>>>>系统业务字典删除参数Req<<<<<<<<<<<<<<<"+JSON.toJSONString(businessDictionaryIdList));
        if (CollectionUtils.isEmpty(businessDictionaryIdList)) {
            return;
        }
        businessDictionaryRepository.updateDeleteStatusByIdListIn(baseConstant.getDeleteStatus(),businessDictionaryIdList);
    }

    /**
     * 系统字典更新
     *
     * @date 2020-07-07
     * @param businessDictUpdateReq 系统字典更新Req
     */
    @Override
    public void updateItem(BusinessDictUpdateReq businessDictUpdateReq){
        log.info(">>>>>>>>>>>>>更新系统业务字典参数Req : {} <<<<<<<<<<<<<<<",
                JSON.toJSONString(businessDictUpdateReq));
        Example businessDictionaryExample = Example.builder(BusinessDictionary.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("businessDictionaryId", businessDictUpdateReq.getBusinessDictionaryId()))
                .build();
        BusinessDictionary businessDictionary = businessDictionaryRepository.selectOneByExample(businessDictionaryExample);
        if (CheckParam.isNull(businessDictionary)) {
            return;
        }
        if(!CheckParam.isNull(businessDictUpdateReq.getDictKey())){
            businessDictionary.setDictKey(businessDictUpdateReq.getDictKey());
        }
        if(!CheckParam.isNull(businessDictUpdateReq.getDictValue())){
            businessDictionary.setDictValue(businessDictUpdateReq.getDictValue());
        }
        if(!CheckParam.isNull(businessDictUpdateReq.getDictType())){
            businessDictionary.setDictType(businessDictUpdateReq.getDictType());
        }
        if(!CheckParam.isNull(businessDictUpdateReq.getDictRemark())){
            businessDictionary.setDictRemark(businessDictUpdateReq.getDictRemark());
        }
        if(!CheckParam.isNull(businessDictUpdateReq.getSortIndex())){
            businessDictionary.setSortIndex(businessDictUpdateReq.getSortIndex());
        }
        businessDictionaryRepository.updateByPrimaryKeySelective(businessDictionary);
    }


    /**
     * 分页查询系统业务字典信息
     *
     * @date 2020-07-07
     * @param pageReq 分页查询系统业务字典Req
     * @return Pagination
     */
    @Override
    public Pagination<BusinessDictionaryResp> queryByPage(
            BusinessDictPageReq pageReq){
        log.info(">>>>>>>>>>>>>系统业务字典分页查询参数Req : {} <<<<<<<<<<<<<<<",
                JSON.toJSONString(pageReq));
        //构建查询条件
        Example pageExample = new Example(BusinessDictionary.class);
        Example.Criteria pageCriteria = pageExample.createCriteria();
        CriteriaBuilder.rigidCriteria(pageCriteria);
        if(!CheckParam.isNull(pageReq.getDictKey())){
            pageCriteria.andEqualTo("dictKey",pageReq.getDictKey());
        }
        if(!CheckParam.isNull(pageReq.getDictType())){
            pageCriteria.andEqualTo("dictType",pageReq.getDictType());
        }
        if(!CheckParam.isNull(pageReq.getDictValue())){
            pageCriteria.andEqualTo("dictValue",pageReq.getDictValue());
        }
        if(!CheckParam.isNull(pageReq.getSortIndex())){
            pageCriteria.andEqualTo("sortIndex",pageReq.getSortIndex());
        }
        pageExample.orderBy("createTime").desc();
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<BusinessDictionary> dictionaryList = businessDictionaryRepository.selectByExample(pageExample);
        if(!CollectionUtils.isEmpty(dictionaryList)) {
            return PageBuilder.buildPageResult(page,dictionaryList);
        }
        List<BusinessDictionaryResp> respList =
                mapperFacade.mapAsList(dictionaryList, BusinessDictionaryResp.class);
        return PageBuilder.buildPageResult(page,respList);
    }


    /**
     * 根据字典值模糊查询字典信息
     *
     * @date 2021/1/21
     * @param dictValue 字段值
     * @return List
     */
    @Override
    public List<BusinessDictionaryResp> queryDictionaryByValue(String dictValue){
        Example businessDictionaryExample = Example.builder(BusinessDictionary.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andLike("dictValue", "%"+dictValue+"%"))
                .build();
        List<BusinessDictionary> dictionaryList =
                businessDictionaryRepository.selectByExample(businessDictionaryExample);

        if (CollectionUtils.isEmpty(dictionaryList)) {
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(dictionaryList,BusinessDictionaryResp.class);
    }

    /**
     * 根据字典分类查询单个字典信息
     *
     * @date 2021/1/21
     * @param dictType 字段类型
     * @return cn.poverty.interaction.resp.dict.BusinessDictionaryResp
     */
    @Override
    public BusinessDictionaryResp querySingleDictByType(String dictType){
        Example businessDictionaryExample = Example.builder(BusinessDictionary.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("dictType", dictType))
                .build();
        BusinessDictionary dict =
                businessDictionaryRepository.selectOneByExample(businessDictionaryExample);
        if (CheckParam.isNull(dict)) {
            return new BusinessDictionaryResp();
        }
        return mapperFacade.map(dict,BusinessDictionaryResp.class);
    }

    /**
     * 根据字典分类查询字典信息
     *
     * @date 2021/1/21
     * @param dictType 字段类型
     * @return List
     */
    @Override
    public List<BusinessDictionaryResp> queryDictionaryByType(String dictType){
        Example businessDictionaryExample = Example.builder(BusinessDictionary.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("dictType", dictType))
                .build();
        List<BusinessDictionary> dictionaryList =
                businessDictionaryRepository.selectByExample(businessDictionaryExample);
        if (CollectionUtils.isEmpty(dictionaryList)) {
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(dictionaryList,BusinessDictionaryResp.class);
    }

    /**
     * 新增系统字典参数
     *
     * @date 2020-07-07
     * @param addReq 新增系统字典参数请求Req
     * @return
     */
    @Override
    public void addItem(BusinessDictAddReq addReq){
        log.info(">>>>>>>>>>>>>新增系统业务字典参数Req : {} <<<<<<<<<<<<<<<",
                JSON.toJSONString(addReq));
        BusinessDictionary businessDictionary = new BusinessDictionary();
        if(!CheckParam.isNull(addReq.getBusinessDictionaryId())){
            businessDictionary.setBusinessDictionaryId(addReq.getBusinessDictionaryId());
        }
        businessDictionary.setDictType(addReq.getDictType());
        businessDictionary.setDictValue(addReq.getDictValue());
        businessDictionary.setDictKey(addReq.getDictKey());
        businessDictionary.setDictRemark(addReq.getDictRemark());
        businessDictionary.setSortIndex(addReq.getSortIndex());
        businessDictionaryRepository.insert(businessDictionary);
    }

}
