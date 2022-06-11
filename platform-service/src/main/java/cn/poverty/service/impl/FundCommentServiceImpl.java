package cn.poverty.service.impl;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.BaseUtil;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import cn.poverty.interaction.helper.PageBuilder;
import cn.poverty.interaction.internal.auth.AuthUserMeta;
import cn.poverty.interaction.req.fund.comment.FundCommentAddReq;
import cn.poverty.interaction.req.fund.comment.FundCommentPageReq;
import cn.poverty.interaction.req.fund.comment.FundCommentUpdateReq;
import cn.poverty.interaction.resp.fund.comment.FundCommentResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.repository.entity.FundComment;
import cn.poverty.repository.entity.ReliefItem;
import cn.poverty.repository.repository.FundCommentRepository;
import cn.poverty.repository.repository.ReliefItemRepository;
import cn.poverty.service.AuthUserService;
import cn.poverty.service.FundCommentService;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 
 * @packageName cn.poverty.service.impl
 * @Description: 救助金服务方法实现
 * @date 2021-10-20
 */
@Service("fundCommentService")
@Slf4j
public class FundCommentServiceImpl implements FundCommentService {

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
    private FundCommentRepository fundCommentRepository;

    @Resource
    private ReliefItemRepository reliefItemRepository;


    /**
     * 新增
     * 
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(FundCommentAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        FundComment entity = mapperFacade.map(addReq, FundComment.class);
        try {
            AuthUserMeta authUserMeta = authUserService.currentUserMeta(true);
            String userName = authUserMeta.getUserName();
            String authUserId = authUserMeta.getAuthUserId();
            BaseUtil.setFieldValueNotNull(entity);
            entity.setFundCommentId(SnowflakeIdWorker.uniqueSequenceStr());
            entity.setOperatorId(authUserId);
            entity.setCommentUserId(authUserId);
            entity.setCommentUserName(userName);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
        }
        fundCommentRepository.insert(entity);
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
        fundCommentRepository.batchUpdateDeleteStatus(baseConstant.getDeleteStatus(),mainIdList);
    }

    /**
     * 分页查询
     * 
     * @date 2021/2/15
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @Override
    public Pagination<FundCommentResp> queryByPage(
            FundCommentPageReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        Example pageExample = new Example(FundComment.class);
        Example.Criteria pageCriteria = pageExample.createCriteria();
        itemCriteriaBuilder.rigidCriteria(pageCriteria,true);
        itemCriteriaBuilder.setContrastFactor(pageCriteria,pageReq);
        setPageCriteria(pageCriteria,pageReq);
        pageExample.orderBy("createTime").desc();
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<FundComment> pageList = fundCommentRepository.selectByExample(pageExample);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<String> itemIdList =
                pageList.stream().map(item -> item.getReliefFundId()).distinct().collect(Collectors.toList());
        Example itemExample = Example.builder(ReliefItem.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("reliefItemId", itemIdList))
                .build();
        List<ReliefItem> reliefItemList = reliefItemRepository.selectByExample(itemExample);
        HashMap<String, ReliefItem> reliefItemHashMap = reliefItemList.stream()
                .collect(Collectors.toMap(ReliefItem::getReliefItemId, a -> a, (k1, k2) -> k1, HashMap::new));
        List<FundCommentResp> respList =
                mapperFacade.mapAsList(pageList, FundCommentResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            ReliefItem reliefItem = reliefItemHashMap.get(item.getReliefFundId());
            if(!CheckParam.isNull(reliefItem)){
                item.setItemName(reliefItem.getItemName());
            }
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
    private void setPageCriteria(Example.Criteria pageCriteria, FundCommentPageReq pageReq){
        if(!CheckParam.isNull(pageReq.getCommentUserName())){
            pageCriteria.andLike("commentUserName","%"+pageReq.getCommentUserName()+"%");
        }
        if(!CheckParam.isNull(pageReq.getReliefFundId())){
            pageCriteria.andEqualTo("reliefFundId",pageReq.getCommentUserName());
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
    public void updateItem(FundCommentUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getFundCommentId();
        Example example = Example.builder(FundComment.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("fundCommentId", mainId))
                .build();
        FundComment result = fundCommentRepository.selectOneByExample(example);
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        fundCommentRepository.updateByPrimaryKeySelective(result);
    }

    /**
     * 设置需要更新的字段
     * 
     * @date 2021/3/30
     * @param updateReq 更新参数
     * @param entity 产业
     */
    private void setNeedUpdateItem(FundComment entity,
                                   FundCommentUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getCommentUserName())){
            entity.setCommentUserName(updateReq.getCommentUserName());
        }
        if(!CheckParam.isNull(updateReq.getCommentContent())){
            entity.setCommentContent(updateReq.getCommentContent());
        }
        if(!CheckParam.isNull(updateReq.getCommentUserId())){
            entity.setCommentUserId(updateReq.getCommentUserId());
        }
        if(!CheckParam.isNull(updateReq.getCommentReply())){
            entity.setCommentReply(updateReq.getCommentReply());
        }
        if(!CheckParam.isNull(updateReq.getReliefFundId())){
            entity.setReliefFundId(updateReq.getReliefFundId());
        }
    }

}
