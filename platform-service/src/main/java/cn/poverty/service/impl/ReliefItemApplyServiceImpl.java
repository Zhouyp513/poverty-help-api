package cn.poverty.service.impl;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.enums.ErrorCode;
import cn.poverty.common.enums.audit.AuditStatusEnum;
import cn.poverty.common.exception.BusinessException;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.BaseUtil;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import cn.poverty.interaction.helper.PageBuilder;
import cn.poverty.interaction.internal.auth.AuthUserMeta;
import cn.poverty.interaction.req.item.apply.ReliefItemApplyAddReq;
import cn.poverty.interaction.req.item.apply.ReliefItemApplyPageReq;
import cn.poverty.interaction.req.item.apply.ReliefItemApplyReq;
import cn.poverty.interaction.req.item.apply.ReliefItemApplyUpdateReq;
import cn.poverty.interaction.resp.item.apply.ReliefItemApplyResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.repository.entity.Attachment;
import cn.poverty.repository.entity.ReliefItemApply;
import cn.poverty.repository.repository.AttachmentRepository;
import cn.poverty.repository.repository.ReliefItemApplyRepository;
import cn.poverty.service.AuthUserService;
import cn.poverty.service.ReliefItemApplyService;
import cn.poverty.service.config.data.ItemCriteriaBuilder;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author
 * @packageName cn.poverty.service.impl
 * @Description: ??????????????????????????????
 * @date 2021-10-20
 */
@Service("reliefItemApplyService")
@Slf4j
public class ReliefItemApplyServiceImpl implements ReliefItemApplyService {

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
    private ReliefItemApplyRepository reliefItemApplyRepository;

    @Resource
    private AttachmentRepository attachmentRepository;

    /**
     * ??????????????????
     * @author
     * @date 2021/2/15
     * @param addReq ??????????????????????????????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void applyItem(ReliefItemApplyReq addReq){
        log.info(">>>>>>>>>>>>>>>>>??????????????????????????????:{} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        AuthUserMeta authUserMeta = authUserService.currentUserMeta(true);
        String authUserId = authUserMeta.getAuthUserId();
        Example example = Example.builder(ReliefItemApply.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("reliefItemId", addReq.getReliefItemId())
                .andEqualTo("applyUserId", authUserId))
                .build();
        Integer count = reliefItemApplyRepository.selectCountByExample(example);
        if(count.compareTo(BigInteger.ZERO.intValue()) > 0){
            throw new BusinessException(ErrorCode.APPLIED_ITEM_ERROR.getCode(),
                    ErrorCode.APPLIED_ITEM_ERROR.getMessage());
        }
        ReliefItemApply entity = mapperFacade.map(addReq, ReliefItemApply.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setReliefItemApplyId(SnowflakeIdWorker.uniqueSequenceStr());
            entity.setOperatorId(authUserId);
            entity.setApplyUserId(authUserId);
            entity.setApplyUserName(authUserMeta.getUserName());
            entity.setAuditStatus(AuditStatusEnum.WAIT_HANDLE.getCode());
            entity.setApplyNo(SnowflakeIdWorker.uniqueStringSequence());
        } catch (Exception e) {
            log.error("??????->??????????????????????????? {} , {} ",e.getMessage(),e);
        }
        reliefItemApplyRepository.insert(entity);
        List<String> attachmentImageList = addReq.getAttachmentImageList();
        if(CollectionUtils.isEmpty(attachmentImageList)){
            return;
        }
        String reliefItemId = entity.getReliefItemId();
        List<Attachment> attachmentList = Lists.newArrayList();
        attachmentImageList.stream().forEach(item -> {
            Attachment attachment = new Attachment();

            //?????????????????? 1:???????????? 2:????????????
            attachment.setAttachType(2);
            attachment.setAttachUrl(item);
            attachment.setOtherId(reliefItemId);
            attachmentList.add(attachment);

        });
        if(!attachmentList.isEmpty()){
            attachmentRepository.insertList(attachmentList);
        }
    }

    /**
     * ??????
     * @author
     * @date 2021/2/15
     * @param addReq ??????Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(ReliefItemApplyAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>??????Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        AuthUserMeta authUserMeta = authUserService.currentUserMeta(true);
        String authUserId = authUserMeta.getAuthUserId();
        Example example = Example.builder(ReliefItemApply.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("reliefItemId", addReq.getReliefItemId())
                .andEqualTo("applyUserId", authUserId))
                .build();
        Integer count = reliefItemApplyRepository.selectCountByExample(example);
        if(count.compareTo(BigInteger.ZERO.intValue()) > 0){
            throw new BusinessException(ErrorCode.APPLIED_ITEM_ERROR.getCode(),
                    ErrorCode.APPLIED_ITEM_ERROR.getMessage());
        }
        ReliefItemApply entity = mapperFacade.map(addReq, ReliefItemApply.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setReliefItemApplyId(SnowflakeIdWorker.uniqueSequenceStr());
            entity.setOperatorId(authUserId);
            entity.setApplyUserId(authUserId);
            entity.setApplyUserName(authUserMeta.getUserName());
            entity.setAuditStatus(AuditStatusEnum.WAIT_HANDLE.getCode());
            entity.setApplyNo(SnowflakeIdWorker.uniqueStringSequence());
        } catch (Exception e) {
            log.error("??????->??????????????????????????? {} , {} ",e.getMessage(),e);
        }
        reliefItemApplyRepository.insert(entity);
        List<String> attachmentImageList = addReq.getAttachmentImageList();
        if(CollectionUtils.isEmpty(attachmentImageList)){
            return;
        }
        String reliefItemId = entity.getReliefItemId();
        List<Attachment> attachmentList = Lists.newArrayList();
        attachmentImageList.stream().forEach(item -> {
            Attachment attachment = new Attachment();

            //?????????????????? 1:???????????? 2:????????????
            attachment.setAttachType(2);
            attachment.setAttachUrl(item);
            attachment.setOtherId(reliefItemId);
            attachmentList.add(attachment);

        });
        if(!attachmentList.isEmpty()){
            attachmentRepository.insertList(attachmentList);
        }
    }

    /**
     * ??????ID????????????
     *
     * @date 2021/2/2
     * @param mainIdList ??????ID??????
     */
    @Override
    public void batchDeleteItem(List<String> mainIdList){
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        reliefItemApplyRepository.batchUpdateDeleteStatus(baseConstant.getDeleteStatus(),mainIdList);
    }

    /**
     * ????????????
     *
     * @date 2021/2/15
     * @param  pageReq ????????????Req
     * @return Pagination
     */
    @Override
    public Pagination<ReliefItemApplyResp> queryByPage(
            ReliefItemApplyPageReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>????????????Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //??????????????????
        Example pageExample = new Example(ReliefItemApply.class);
        Example.Criteria pageCriteria = pageExample.createCriteria();
        itemCriteriaBuilder.rigidCriteria(pageCriteria,true);
        setPageCriteria(pageCriteria,pageReq);
        pageExample.orderBy("createTime").desc();
        //????????????
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<ReliefItemApply> pageList = reliefItemApplyRepository.selectByExample(pageExample);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<String> mainIdList =
                pageList.stream().map(item -> item.getReliefItemId()).distinct().collect(Collectors.toList());
        Example attachmentExample = Example.builder(Attachment.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andIn("otherId", mainIdList))
                .build();
        List<Attachment> attachmentList = attachmentRepository.selectByExample(attachmentExample);
        Map<String, List<Attachment>> attachmentHashMap = attachmentList.stream()
                .collect(Collectors.groupingBy(Attachment::getOtherId));
        List<ReliefItemApplyResp> respList =
                mapperFacade.mapAsList(pageList, ReliefItemApplyResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            String reliefItemId = item.getReliefItemId();
            List<Attachment> attachList = attachmentHashMap.get(reliefItemId);
            if(!CollectionUtils.isEmpty(attachList)){
                List<String> urlList = attachList.stream().map(attach -> attach.getAttachUrl())
                        .collect(Collectors.toList());
                item.setAttachmentImageList(urlList);
            }
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
        });
        return PageBuilder.buildPageResult(page,respList);
    }

    /**
     * ??????????????????
     *
     * @date 2021/5/31
     * @param pageCriteria ????????????
     * @param pageReq ????????????
     * @return
     */
    private void setPageCriteria(Example.Criteria pageCriteria, ReliefItemApplyPageReq pageReq){
        Boolean needAll = pageReq.getNeedAll();
        if(!needAll){
            String authUserId = authUserService.currentAuthUserId();
            pageCriteria.andEqualTo("applyUserId",authUserId);
        }
        if(!CheckParam.isNull(pageReq.getApplyNo())){
            pageCriteria.andLike("applyNo","%"+pageReq.getApplyNo()+"%");
        }
        if(!CheckParam.isNull(pageReq.getApplyReason())){
            pageCriteria.andLike("applyReason","%"+pageReq.getApplyReason()+"%");
        }
        if(!CheckParam.isNull(pageReq.getReliefItemId())){
            pageCriteria.andEqualTo("reliefItemId",pageReq.getReliefItemId());
        }
        if(!CheckParam.isNull(pageReq.getAuditStatus())){
            pageCriteria.andEqualTo("auditStatus",pageReq.getAuditStatus());
        }
    }


    /**
     * ??????
     *
     * @date 2021/4/2
     * @param updateReq ??????????????????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(ReliefItemApplyUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>?????????????????? {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getReliefItemApplyId();
        Example example = Example.builder(ReliefItemApply.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("reliefItemApplyId", mainId))
                .build();
        ReliefItemApply result = reliefItemApplyRepository.selectOneByExample(example);
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        reliefItemApplyRepository.updateByPrimaryKeySelective(result);
    }

    /**
     * ???????????????????????????
     *
     * @date 2021/3/30
     * @param updateReq ????????????
     * @param entity ??????
     */
    private void setNeedUpdateItem(ReliefItemApply entity,
                                   ReliefItemApplyUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getApplyNo())){
            entity.setApplyNo(updateReq.getApplyNo());
        }
        if(!CheckParam.isNull(updateReq.getReliefItemId())){
            entity.setReliefItemId(updateReq.getReliefItemId());
        }
        if(!CheckParam.isNull(updateReq.getReliefItemApplyId())){
            entity.setReliefItemApplyId(updateReq.getReliefItemApplyId());
        }
        if(!CheckParam.isNull(updateReq.getItemAmount())){
            entity.setItemAmount(updateReq.getItemAmount());
        }
        if(!CheckParam.isNull(updateReq.getAuditStatus())){
            entity.setAuditStatus(updateReq.getAuditStatus());
        }
    }




}
