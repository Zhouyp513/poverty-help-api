package cn.poverty.service.impl;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.hutool.core.util.StrUtil;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.constants.PlatformConstant;
import cn.poverty.common.enums.ErrorCode;
import cn.poverty.common.enums.audit.AuditStatusEnum;
import cn.poverty.common.exception.BusinessException;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.BaseUtil;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import cn.poverty.interaction.helper.PageBuilder;
import cn.poverty.interaction.internal.auth.AuthUserMeta;
import cn.poverty.interaction.req.fund.apply.FundApplyReq;
import cn.poverty.interaction.req.fund.apply.ReliefFundApplyAddReq;
import cn.poverty.interaction.req.fund.apply.ReliefFundApplyPageReq;
import cn.poverty.interaction.req.fund.apply.ReliefFundApplyUpdateReq;
import cn.poverty.interaction.resp.fund.apply.ReliefFundApplyResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.repository.entity.Attachment;
import cn.poverty.repository.entity.BusinessDictionary;
import cn.poverty.repository.entity.ReliefFundApply;
import cn.poverty.repository.repository.AttachmentRepository;
import cn.poverty.repository.repository.BusinessDictionaryRepository;
import cn.poverty.repository.repository.ReliefFundApplyRepository;
import cn.poverty.service.AuthUserService;
import cn.poverty.service.ReliefFundApplyService;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author
 * @packageName cn.poverty.service.impl
 * @Description: 救助金服务方法实现
 * @date 2021-10-20
 */
@Service("reliefFundApplyService")
@Slf4j
public class ReliefFundApplyServiceImpl implements ReliefFundApplyService {

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
    private ReliefFundApplyRepository reliefFundApplyRepository;

    @Resource
    private BusinessDictionaryRepository businessDictionaryRepository;

    @Resource
    private AttachmentRepository attachmentRepository;

    /**
     * 申请救助金
     * @author
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void applyFund(FundApplyReq addReq){
        log.info(">>>>>>>>>>>>>>>>>申请救助金请求Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        AuthUserMeta authUserMeta = authUserService.currentUserMeta(true);
        String authUserId = authUserMeta.getAuthUserId();
        Example example = Example.builder(ReliefFundApply.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("reliefFundId", addReq.getReliefFundId())
                .andEqualTo("applyUserId", authUserId))
                .build();
        Integer count = reliefFundApplyRepository.selectCountByExample(example);
        if(count.compareTo(BigInteger.ZERO.intValue()) > 0){
            throw new BusinessException(ErrorCode.APPLIED_ITEM_ERROR.getCode(),
                    ErrorCode.APPLIED_ITEM_ERROR.getMessage());
        }
        ReliefFundApply entity = mapperFacade.map(addReq, ReliefFundApply.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setReliefFundApplyId(SnowflakeIdWorker.uniqueSequenceStr());
            entity.setOperatorId(authUserId);
            entity.setApplyUserId(authUserId);
            entity.setApplyUserName(authUserMeta.getUserName());
            entity.setAuditStatus(AuditStatusEnum.WAIT_HANDLE.getCode());
            entity.setApplyNo(SnowflakeIdWorker.uniqueStringSequence());
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
        }
        reliefFundApplyRepository.insert(entity);
        List<String> attachmentImageList = addReq.getAttachmentImageList();
        if(CollectionUtils.isEmpty(attachmentImageList)){
            return;
        }
        String reliefFundApplyId = entity.getReliefFundApplyId();
        List<Attachment> attachmentList = Lists.newArrayList();
        attachmentImageList.stream().forEach(item -> {
            Attachment attachment = new Attachment();

            //附件业务类型 1:扶贫资金 2:扶贫项目
            attachment.setAttachType(1);
            attachment.setAttachUrl(item);
            attachment.setOtherId(reliefFundApplyId);
            attachmentList.add(attachment);

        });
        if(!attachmentList.isEmpty()){
            attachmentRepository.insertList(attachmentList);
        }
    }

    /**
     * 新增
     * @author
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(ReliefFundApplyAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        AuthUserMeta authUserMeta = authUserService.currentUserMeta(true);
        String authUserId = authUserMeta.getAuthUserId();
        Example example = Example.builder(ReliefFundApply.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("reliefFundId", addReq.getReliefFundId())
                .andEqualTo("applyUserId", authUserId))
                .build();
        Integer count = reliefFundApplyRepository.selectCountByExample(example);
        if(count.compareTo(BigInteger.ZERO.intValue()) > 0){
            throw new BusinessException(ErrorCode.APPLIED_ITEM_ERROR.getCode(),
                    ErrorCode.APPLIED_ITEM_ERROR.getMessage());
        }
        ReliefFundApply entity = mapperFacade.map(addReq, ReliefFundApply.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setReliefFundApplyId(SnowflakeIdWorker.uniqueSequenceStr());
            entity.setOperatorId(authUserId);
            entity.setApplyUserId(authUserId);
            entity.setApplyUserName(authUserMeta.getUserName());
            entity.setAuditStatus(AuditStatusEnum.WAIT_HANDLE.getCode());
            entity.setApplyNo(SnowflakeIdWorker.uniqueStringSequence());
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
        }
        reliefFundApplyRepository.insert(entity);
        List<String> attachmentImageList = addReq.getAttachmentImageList();
        if(CollectionUtils.isEmpty(attachmentImageList)){
            return;
        }
        String reliefFundApplyId = entity.getReliefFundApplyId();
        List<Attachment> attachmentList = Lists.newArrayList();
        attachmentImageList.stream().forEach(item -> {
            Attachment attachment = new Attachment();

            //附件业务类型 1:扶贫资金 2:扶贫项目
            attachment.setAttachType(1);
            attachment.setAttachUrl(item);
            attachment.setOtherId(reliefFundApplyId);
            attachmentList.add(attachment);

        });
        if(!attachmentList.isEmpty()){
            attachmentRepository.insertList(attachmentList);
        }
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
        reliefFundApplyRepository.batchUpdateDeleteStatus(baseConstant.getDeleteStatus(),mainIdList);
    }

    /**
     * 分页查询
     *
     * @date 2021/2/15
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @Override
    public Pagination<ReliefFundApplyResp> queryByPage(
            ReliefFundApplyPageReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        Example pageExample = new Example(ReliefFundApply.class);
        Example.Criteria pageCriteria = pageExample.createCriteria();
        itemCriteriaBuilder.rigidCriteria(pageCriteria,true);
        setPageCriteria(pageCriteria,pageReq);
        pageExample.orderBy("createTime").desc();
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<ReliefFundApply> pageList = reliefFundApplyRepository.selectByExample(pageExample);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<String> mainIdList =
                pageList.stream().map(item -> item.getReliefFundApplyId()).distinct().collect(Collectors.toList());
        Example attachmentExample = Example.builder(Attachment.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andIn("otherId", mainIdList))
                .build();
        List<Attachment> attachmentList = attachmentRepository.selectByExample(attachmentExample);
        Map<String, List<Attachment>> attachmentHashMap = attachmentList.stream()
                .collect(Collectors.groupingBy(Attachment::getOtherId));
        List<ReliefFundApplyResp> respList =
                mapperFacade.mapAsList(pageList, ReliefFundApplyResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            String reliefFundApplyId = item.getReliefFundApplyId();
            List<Attachment> attachList = attachmentHashMap.get(reliefFundApplyId);
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
     * 设置分页条件
     *
     * @date 2021/5/31
     * @param pageCriteria 查询条件
     * @param pageReq 分页插件
     * @return
     */
    private void setPageCriteria(Example.Criteria pageCriteria, ReliefFundApplyPageReq pageReq){
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
        if(!CheckParam.isNull(pageReq.getReliefFundId())){
            pageCriteria.andEqualTo("reliefFundId",pageReq.getReliefFundId());
        }
        if(!CheckParam.isNull(pageReq.getAuditStatus())){
            pageCriteria.andEqualTo("auditStatus",pageReq.getAuditStatus());
        }
        if(!CheckParam.isNull(pageReq.getNation())){
            pageCriteria.andEqualTo("nation",pageReq.getNation());
        }
        if(!CheckParam.isNull(pageReq.getRealName())){
            pageCriteria.andEqualTo("realName",pageReq.getRealName());
        }
        if(!CheckParam.isNull(pageReq.getGender())){
            pageCriteria.andEqualTo("gender",pageReq.getGender());
        }
        if(!CheckParam.isNull(pageReq.getBirthday())){
            pageCriteria.andEqualTo("birthday",pageReq.getBirthday());
        }
        if(!CheckParam.isNull(pageReq.getAge())){
            pageCriteria.andEqualTo("age",pageReq.getAge());
        }
        if(!CheckParam.isNull(pageReq.getIdCardNum())){
            pageCriteria.andEqualTo("idCardNum",pageReq.getIdCardNum());
        }
        if(!CheckParam.isNull(pageReq.getPhoneNum())){
            pageCriteria.andEqualTo("phoneNum",pageReq.getPhoneNum());
        }
        if(!CheckParam.isNull(pageReq.getMaritalStatus())){
            pageCriteria.andEqualTo("maritalStatus",pageReq.getMaritalStatus());
        }
        if(!CheckParam.isNull(pageReq.getPoliticsStatus())){
            pageCriteria.andEqualTo("politicsStatus",pageReq.getPoliticsStatus());
        }
        if(!CheckParam.isNull(pageReq.getHouseholdType())){
            pageCriteria.andEqualTo("householdType",pageReq.getHouseholdType());
        }
        if(!CheckParam.isNull(pageReq.getNativePlace())){
            pageCriteria.andEqualTo("nativePlace",pageReq.getNativePlace());
        }
        if(!CheckParam.isNull(pageReq.getAuditStatus())){
            pageCriteria.andEqualTo("auditStatus",pageReq.getAuditStatus());
        }
        if(!CheckParam.isNull(pageReq.getNativeAddress())){
            pageCriteria.andLike("nativeAddress","%"+pageReq.getNativeAddress());
        }
        if(!CheckParam.isNull(pageReq.getContactAddress())){
            pageCriteria.andLike("contactAddress","%"+pageReq.getContactAddress());
        }
        if(!CheckParam.isNull(pageReq.getEmergencyContact())){
            pageCriteria.andEqualTo("emergencyContact","%"+pageReq.getEmergencyContact());
        }
        if(!CheckParam.isNull(pageReq.getEmergencyRelation())){
            pageCriteria.andEqualTo("emergencyRelation",pageReq.getEmergencyRelation());
        }
        if(!CheckParam.isNull(pageReq.getEmergencyPhone())){
            pageCriteria.andEqualTo("emergencyPhone",pageReq.getEmergencyPhone());
        }
        if(!CheckParam.isNull(pageReq.getHealthyCondition())){
            pageCriteria.andEqualTo("healthyCondition",pageReq.getHealthyCondition());
        }
        if(!CheckParam.isNull(pageReq.getChildrenStatus())){
            pageCriteria.andEqualTo("childrenStatus",pageReq.getChildrenStatus());
        }
        if(!CheckParam.isNull(pageReq.getDisabilityStatus())){
            pageCriteria.andEqualTo("disabilityStatus",pageReq.getDisabilityStatus());
        }
        if(!CheckParam.isNull(pageReq.getFundingLowStatus())){
            pageCriteria.andEqualTo("fundingLowStatus",pageReq.getFundingLowStatus());
        }
        if(!CheckParam.isNull(pageReq.getUnemploymentStatus())){
            pageCriteria.andEqualTo("unemploymentStatus",pageReq.getUnemploymentStatus());
        }
        if(!CheckParam.isNull(pageReq.getOccupation())){
            pageCriteria.andEqualTo("occupation",pageReq.getOccupation());
        }
        if(!CheckParam.isNull(pageReq.getCompanyAddress())){
            pageCriteria.andEqualTo("companyAddress",pageReq.getCompanyAddress());
        }
        if(!CheckParam.isNull(pageReq.getLowIncomeStatus())){
            pageCriteria.andEqualTo("lowIncomeStatus",pageReq.getLowIncomeStatus());
        }
        if(!CheckParam.isNull(pageReq.getAverageMonthlyEarnings())){
            pageCriteria.andEqualTo("averageMonthlyEarnings",pageReq.getAverageMonthlyEarnings());
        }
        if(!CheckParam.isNull(pageReq.getAverageYearEarnings())){
            pageCriteria.andEqualTo("averageYearEarnings",pageReq.getAverageYearEarnings());
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
    public void updateItem(ReliefFundApplyUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getReliefFundApplyId();
        Example example = Example.builder(ReliefFundApply.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("reliefFundApplyId", mainId))
                .build();
        ReliefFundApply result = reliefFundApplyRepository.selectOneByExample(example);
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        reliefFundApplyRepository.updateByPrimaryKeySelective(result);
    }

    /**
     * 设置需要更新的字段
     *
     * @date 2021/3/30
     * @param updateReq 更新参数
     * @param entity 产业
     */
    private void setNeedUpdateItem(ReliefFundApply entity,
                                   ReliefFundApplyUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getApplyNo())){
            entity.setApplyNo(updateReq.getApplyNo());
        }
        if(!CheckParam.isNull(updateReq.getReliefFundId())){
            entity.setReliefFundId(updateReq.getReliefFundId());
        }
        if(!CheckParam.isNull(updateReq.getReliefFundApplyId())){
            entity.setReliefFundApplyId(updateReq.getReliefFundApplyId());
        }
        if(!CheckParam.isNull(updateReq.getItemAmount())){
            entity.setItemAmount(updateReq.getItemAmount());
        }
        if(!CheckParam.isNull(updateReq.getAuditStatus())){
            if(StrUtil.equalsAnyIgnoreCase(updateReq.getAuditStatus(),
                    AuditStatusEnum.GRANTED.getCode())){
                Example businessDictionaryExample = Example.builder(BusinessDictionary.class).where(Sqls.custom()
                        .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                        .andEqualTo("dictType", PlatformConstant.TOTAL_FUND_AMOUNT))
                        .build();
                BusinessDictionary dict =
                        businessDictionaryRepository.selectOneByExample(businessDictionaryExample);
                if(!CheckParam.isNull(dict)){
                    String newValue =
                            BigDecimal.valueOf(Double.valueOf(dict.getDictValue()))
                                    .subtract(entity.getItemAmount()).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
                    dict.setDictValue(newValue);
                    businessDictionaryRepository.updateByPrimaryKeySelective(dict);
                }
            }
            entity.setAuditStatus(updateReq.getAuditStatus());
        }
        if(!CheckParam.isNull(updateReq.getNation())){
            entity.setNation(updateReq.getNation());
        }
        if(!CheckParam.isNull(updateReq.getRealName())){
            entity.setRealName(updateReq.getRealName());
        }
        if(!CheckParam.isNull(updateReq.getGender())){
            entity.setGender(updateReq.getGender());
        }
        if(!CheckParam.isNull(updateReq.getBirthday())){
            entity.setBirthday(updateReq.getBirthday());
        }
        if(!CheckParam.isNull(updateReq.getAge())){
            entity.setAge(updateReq.getAge());
        }
        if(!CheckParam.isNull(updateReq.getIdCardNum())){
            entity.setIdCardNum(updateReq.getIdCardNum());
        }
        if(!CheckParam.isNull(updateReq.getPhoneNum())){
            entity.setPhoneNum(updateReq.getPhoneNum());
        }
        if(!CheckParam.isNull(updateReq.getMaritalStatus())){
            entity.setMaritalStatus(updateReq.getMaritalStatus());
        }
        if(!CheckParam.isNull(updateReq.getPoliticsStatus())){
            entity.setPoliticsStatus(updateReq.getPoliticsStatus());
        }
        if(!CheckParam.isNull(updateReq.getHouseholdType())){
            entity.setHouseholdType(updateReq.getHouseholdType());
        }
        if(!CheckParam.isNull(updateReq.getNativePlace())){
            entity.setNativePlace(updateReq.getNativePlace());
        }
        if(!CheckParam.isNull(updateReq.getNativeAddress())){
            entity.setNativeAddress(updateReq.getNativeAddress());
        }
        if(!CheckParam.isNull(updateReq.getContactAddress())){
            entity.setContactAddress(updateReq.getContactAddress());
        }
        if(!CheckParam.isNull(updateReq.getEmergencyContact())){
            entity.setEmergencyContact(updateReq.getEmergencyContact());
        }
        if(!CheckParam.isNull(updateReq.getEmergencyRelation())){
            entity.setEmergencyRelation(updateReq.getEmergencyRelation());
        }
        if(!CheckParam.isNull(updateReq.getEmergencyPhone())){
            entity.setEmergencyPhone(updateReq.getEmergencyPhone());
        }
        if(!CheckParam.isNull(updateReq.getHealthyCondition())){
            entity.setHealthyCondition(updateReq.getHealthyCondition());
        }
        if(!CheckParam.isNull(updateReq.getChildrenStatus())){
            entity.setChildrenStatus(updateReq.getChildrenStatus());
        }
        if(!CheckParam.isNull(updateReq.getDisabilityStatus())){
            entity.setDisabilityStatus(updateReq.getDisabilityStatus());
        }
        if(!CheckParam.isNull(updateReq.getFundingLowStatus())){
            entity.setFundingLowStatus(updateReq.getFundingLowStatus());
        }
        if(!CheckParam.isNull(updateReq.getUnemploymentStatus())){
            entity.setUnemploymentStatus(updateReq.getUnemploymentStatus());
        }
        if(!CheckParam.isNull(updateReq.getOccupation())){
            entity.setOccupation(updateReq.getOccupation());
        }
        if(!CheckParam.isNull(updateReq.getCompanyAddress())){
            entity.setCompanyAddress(updateReq.getCompanyAddress());
        }
        if(!CheckParam.isNull(updateReq.getLowIncomeStatus())){
            entity.setLowIncomeStatus(updateReq.getLowIncomeStatus());
        }
        if(!CheckParam.isNull(updateReq.getAverageMonthlyEarnings())){
            entity.setAverageMonthlyEarnings(updateReq.getAverageMonthlyEarnings());
        }
        if(!CheckParam.isNull(updateReq.getAverageYearEarnings())){
            entity.setAverageYearEarnings(updateReq.getAverageYearEarnings());
        }


    }
}
