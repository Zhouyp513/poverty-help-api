package cn.poverty.service.impl;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.BaseUtil;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import cn.poverty.interaction.helper.PageBuilder;
import cn.poverty.interaction.internal.auth.AuthUserMeta;
import cn.poverty.interaction.req.trace.TraceRecordAddReq;
import cn.poverty.interaction.req.trace.TraceRecordPageReq;
import cn.poverty.interaction.req.trace.TraceRecordUpdateReq;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.interaction.resp.trace.TraceRecordResp;
import cn.poverty.repository.entity.TraceRecord;
import cn.poverty.repository.repository.TraceRecordRepository;
import cn.poverty.service.AuthUserService;
import cn.poverty.service.TraceRecordService;
import cn.poverty.service.config.data.ItemCriteriaBuilder;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 
 * @packageName cn.poverty.service.impl
 * @Description: 救助金服务方法实现
 * @date 2021-10-20
 */
@Service("traceRecordService")
@Slf4j
public class TraceRecordServiceImpl implements TraceRecordService {

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private IExcelDictHandler excelDictHandler;

    @Resource
    private ItemCriteriaBuilder itemCriteriaBuilder;

    @Resource
    private TraceRecordRepository traceRecordRepository;


    /**
     * 新增
     * 
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(TraceRecordAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        TraceRecord entity = mapperFacade.map(addReq, TraceRecord.class);
        try {
            AuthUserMeta authUserMeta = authUserService.currentUserMeta(true);
            String authUserId = authUserMeta.getAuthUserId();
            BaseUtil.setFieldValueNotNull(entity);
            entity.setTraceRecordId(SnowflakeIdWorker.uniqueSequenceStr());
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
        }
        traceRecordRepository.insert(entity);
    }

    /**
     * 主键ID集合批量
     * 
     * @date 2021/2/2
     * @param mainIdList 主键ID集合
     */
    @Override
    public void batchDeleteItem(List<String> mainIdList){
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        traceRecordRepository.batchUpdateDeleteStatus(baseConstant.getDeleteStatus(),mainIdList);
    }


    /**
      * 查询所有项目
      * 
      * @date 2021/10/25
      * @return java.util.List
      */
    @Override
    public List<TraceRecordResp> queryAllItem(){
        Example example = Example.builder(TraceRecord.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus()))
                .build();
        List<TraceRecord> traceRecordList = traceRecordRepository.selectByExample(example);
        return mapperFacade.mapAsList(traceRecordList,TraceRecordResp.class);
    }

    /**
     * 分页查询
     * 
     * @date 2021/2/15
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @Override
    public Pagination<TraceRecordResp> queryByPage(
            TraceRecordPageReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        Example pageExample = new Example(TraceRecord.class);
        Example.Criteria pageCriteria = pageExample.createCriteria();
        itemCriteriaBuilder.rigidCriteria(pageCriteria,true);
        setPageCriteria(pageCriteria,pageReq);
        pageExample.orderBy("createTime").desc();
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<TraceRecord> pageList = traceRecordRepository.selectByExample(pageExample);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<TraceRecordResp> respList =
                mapperFacade.mapAsList(pageList, TraceRecordResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
        });
        return PageBuilder.buildPageResult(page,respList);
    }

    /**
     * 设置分页条件
     * 
     * @date 2021/5/31
     * @param pageCriteria 查询条件
     * @param pageReq 分页插件
     * @return
     */
    private void setPageCriteria(Example.Criteria pageCriteria, TraceRecordPageReq pageReq){
        if(!CheckParam.isNull(pageReq.getTraceContent())){
            pageCriteria.andLike("traceContent","%"+pageReq.getTraceContent()+"%");
        }
        if(!CheckParam.isNull(pageReq.getTraceType())){
            pageCriteria.andLike("getTraceType","%"+pageReq.getTraceType()+"%");
        }
    }


    /**
     * 更新
     * 
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(TraceRecordUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getTraceRecordId();
        Example example = Example.builder(TraceRecord.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("traceRecordId", mainId))
                .build();
        TraceRecord result = traceRecordRepository.selectOneByExample(example);
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        traceRecordRepository.updateByPrimaryKeySelective(result);
    }

    /**
     * 设置需要更新的字段
     * 
     * @date 2021/3/30
     * @param updateReq 更新参数
     * @param entity 产业
     */
    private void setNeedUpdateItem(TraceRecord entity,
                                   TraceRecordUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getTraceContent())){
            entity.setTraceContent(updateReq.getTraceContent());
        }
        if(!CheckParam.isNull(updateReq.getTraceType())){
            entity.setTraceType(updateReq.getTraceType());
        }
    }
}
