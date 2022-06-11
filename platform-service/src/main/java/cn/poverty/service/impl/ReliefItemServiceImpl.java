package cn.poverty.service.impl;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.BaseUtil;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import cn.poverty.interaction.helper.PageBuilder;
import cn.poverty.interaction.internal.auth.AuthUserMeta;
import cn.poverty.interaction.req.item.ReliefItemAddReq;
import cn.poverty.interaction.req.item.ReliefItemPageReq;
import cn.poverty.interaction.req.item.ReliefItemUpdateReq;
import cn.poverty.interaction.resp.item.ReliefItemResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.repository.entity.ReliefItem;
import cn.poverty.repository.repository.ReliefItemRepository;
import cn.poverty.service.AuthUserService;
import cn.poverty.service.ReliefItemService;
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
@Service("reliefItemService")
@Slf4j
public class ReliefItemServiceImpl implements ReliefItemService {

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
    private ReliefItemRepository reliefItemRepository;


    /**
     * 新增
     * 
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(ReliefItemAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        ReliefItem entity = mapperFacade.map(addReq, ReliefItem.class);
        try {
            AuthUserMeta authUserMeta = authUserService.currentUserMeta(true);
            String authUserId = authUserMeta.getAuthUserId();
            BaseUtil.setFieldValueNotNull(entity);
            entity.setReliefItemId(SnowflakeIdWorker.uniqueSequenceStr());
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
        }
        reliefItemRepository.insert(entity);
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
        reliefItemRepository.batchUpdateDeleteStatus(baseConstant.getDeleteStatus(),mainIdList);
    }


    /**
      * 查询所有项目
      * 
      * @date 2021/10/25
      * @return java.util.List
      */
    @Override
    public List<ReliefItemResp> queryAllItem(){
        Example example = Example.builder(ReliefItem.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus()))
                .build();
        List<ReliefItem> reliefItemList = reliefItemRepository.selectByExample(example);
        return mapperFacade.mapAsList(reliefItemList,ReliefItemResp.class);
    }

    /**
     * 分页查询
     * 
     * @date 2021/2/15
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @Override
    public Pagination<ReliefItemResp> queryByPage(
            ReliefItemPageReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        Example pageExample = new Example(ReliefItem.class);
        Example.Criteria pageCriteria = pageExample.createCriteria();
        itemCriteriaBuilder.rigidCriteria(pageCriteria,true);
        setPageCriteria(pageCriteria,pageReq);
        pageExample.orderBy("createTime").desc();
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<ReliefItem> pageList = reliefItemRepository.selectByExample(pageExample);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<ReliefItemResp> respList =
                mapperFacade.mapAsList(pageList, ReliefItemResp.class);
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
    private void setPageCriteria(Example.Criteria pageCriteria, ReliefItemPageReq pageReq){
        if(!CheckParam.isNull(pageReq.getItemName())){
            pageCriteria.andLike("itemName","%"+pageReq.getItemName()+"%");
        }
        if(!CheckParam.isNull(pageReq.getPrincipal())){
            pageCriteria.andLike("principal","%"+pageReq.getPrincipal()+"%");
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
    public void updateItem(ReliefItemUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getReliefItemId();
        Example example = Example.builder(ReliefItem.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("reliefItemId", mainId))
                .build();
        ReliefItem result = reliefItemRepository.selectOneByExample(example);
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        reliefItemRepository.updateByPrimaryKeySelective(result);
    }

    /**
     * 设置需要更新的字段
     * 
     * @date 2021/3/30
     * @param updateReq 更新参数
     * @param entity 产业
     */
    private void setNeedUpdateItem(ReliefItem entity,
                                   ReliefItemUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getItemName())){
            entity.setItemName(updateReq.getItemName());
        }
        if(!CheckParam.isNull(updateReq.getItemAddress())){
            entity.setItemAddress(updateReq.getItemAddress());
        }
        if(!CheckParam.isNull(updateReq.getItemAmount())){
            entity.setItemAmount(updateReq.getItemAmount());
        }
        if(!CheckParam.isNull(updateReq.getBeginTime())){
            entity.setBeginTime(updateReq.getBeginTime());
        }
        if(!CheckParam.isNull(updateReq.getPrincipal())){
            entity.setPrincipal(updateReq.getPrincipal());
        }
        if(!CheckParam.isNull(updateReq.getLimitCount())){
            entity.setLimitCount(updateReq.getLimitCount());
        }
        if(!CheckParam.isNull(updateReq.getRequirements())){
            entity.setRequirements(updateReq.getRequirements());
        }
    }




}
