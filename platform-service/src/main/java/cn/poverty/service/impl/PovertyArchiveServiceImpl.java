package cn.poverty.service.impl;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.BaseUtil;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import cn.poverty.interaction.helper.PageBuilder;
import cn.poverty.interaction.internal.auth.AuthUserMeta;

import cn.poverty.interaction.req.archive.PovertyArchiveAddReq;
import cn.poverty.interaction.req.archive.PovertyArchivePageReq;
import cn.poverty.interaction.req.archive.PovertyArchiveUpdateReq;
import cn.poverty.interaction.resp.archive.PovertyArchiveResp;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.repository.entity.AuthUser;
import cn.poverty.repository.entity.PovertyArchive;
import cn.poverty.repository.repository.AuthUserRepository;
import cn.poverty.repository.repository.PovertyArchiveRepository;
import cn.poverty.service.AuthUserService;
import cn.poverty.service.PovertyArchiveService;
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
@Service("povertyArchiveService")
@Slf4j
public class PovertyArchiveServiceImpl implements PovertyArchiveService {

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
    private PovertyArchiveRepository povertyArchiveRepository;

    @Resource
    private AuthUserRepository authUserRepository;


    /**
     * 新增
     * 
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(PovertyArchiveAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        Example authUserExample = Example.builder(AuthUser.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("authUserId", addReq.getArchiveUserId()))
                .build();
        AuthUser authUser = authUserRepository.selectOneByExample(authUserExample);
        PovertyArchive entity = mapperFacade.map(addReq, PovertyArchive.class);
        try {
            AuthUserMeta authUserMeta = authUserService.currentUserMeta(true);
            String authUserId = authUserMeta.getAuthUserId();
            BaseUtil.setFieldValueNotNull(entity);
            entity.setPovertyArchiveId(SnowflakeIdWorker.uniqueSequenceStr());
            entity.setOperatorId(authUserId);
            if(!CheckParam.isNull(authUser)){
                entity.setArchiveUserId(authUser.getAuthUserId());
                entity.setArchiveUserName(authUser.getUserName());
            }
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
        }
        povertyArchiveRepository.insert(entity);
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
        povertyArchiveRepository.batchUpdateDeleteStatus(baseConstant.getDeleteStatus(),mainIdList);
    }


    /**
      * 查询所有项目
      * 
      * @date 2021/10/25
      * @return java.util.List
      */
    @Override
    public List<PovertyArchiveResp> queryAllItem(){
        Example example = Example.builder(PovertyArchive.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus()))
                .build();
        List<PovertyArchive> povertyArchiveList = povertyArchiveRepository.selectByExample(example);
        return mapperFacade.mapAsList(povertyArchiveList,PovertyArchiveResp.class);
    }

    /**
     * 分页查询
     * 
     * @date 2021/2/15
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @Override
    public Pagination<PovertyArchiveResp> queryByPage(
            PovertyArchivePageReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        Example pageExample = new Example(PovertyArchive.class);
        Example.Criteria pageCriteria = pageExample.createCriteria();
        itemCriteriaBuilder.rigidCriteria(pageCriteria,true);
        setPageCriteria(pageCriteria,pageReq);
        pageExample.orderBy("createTime").desc();
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<PovertyArchive> pageList = povertyArchiveRepository.selectByExample(pageExample);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<PovertyArchiveResp> respList =
                mapperFacade.mapAsList(pageList, PovertyArchiveResp.class);
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
    private void setPageCriteria(Example.Criteria pageCriteria, PovertyArchivePageReq pageReq){
        if(!CheckParam.isNull(pageReq.getArchiveUserId())){
            pageCriteria.andEqualTo("archiveUserId",pageReq.getArchiveUserId());
        }
        if(!CheckParam.isNull(pageReq.getArchiveUserName())){
            pageCriteria.andLike("archiveUserName","%"+pageReq.getArchiveUserName()+"%");
        }
        if(!CheckParam.isNull(pageReq.getHelpMain())){
            pageCriteria.andLike("helpMain","%"+pageReq.getHelpMain()+"%");
        }
        if(!CheckParam.isNull(pageReq.getPairingContent())){
            pageCriteria.andLike("pairingContent","%"+pageReq.getPairingContent()+"%");
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
    public void updateItem(PovertyArchiveUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getPovertyArchiveId();
        Example example = Example.builder(PovertyArchive.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("povertyArchiveId", mainId))
                .build();
        PovertyArchive result = povertyArchiveRepository.selectOneByExample(example);
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        povertyArchiveRepository.updateByPrimaryKeySelective(result);
    }

    /**
     * 设置需要更新的字段
     * 
     * @date 2021/3/30
     * @param updateReq 更新参数
     * @param entity 产业
     */
    private void setNeedUpdateItem(PovertyArchive entity,
                                   PovertyArchiveUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getArchiveUserId())){
            entity.setArchiveUserId(updateReq.getArchiveUserId());
            Example authUserExample = Example.builder(AuthUser.class).where(Sqls.custom()
                    .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                    .andEqualTo("authUserId", updateReq.getArchiveUserId()))
                    .build();
            AuthUser authUser = authUserRepository.selectOneByExample(authUserExample);
            if(!CheckParam.isNull(authUser)){
                entity.setArchiveUserName(authUser.getUserName());
            }
        }
        if(!CheckParam.isNull(updateReq.getPairingContent())){
            entity.setPairingContent(updateReq.getPairingContent());
        }
        if(!CheckParam.isNull(updateReq.getHelpMain())){
            entity.setHelpMain(updateReq.getHelpMain());
        }
        if(!CheckParam.isNull(updateReq.getArchiveUserName())){
            entity.setArchiveUserName(updateReq.getArchiveUserName());
        }
    }
}
