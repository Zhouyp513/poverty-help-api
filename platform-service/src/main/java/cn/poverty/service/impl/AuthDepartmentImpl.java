package cn.poverty.service.impl;


import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.entity.Tree;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.CriteriaBuilder;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.common.TreeUtil;
import cn.poverty.interaction.req.department.DepartmentAddReq;
import cn.poverty.interaction.req.department.DepartmentQueryReq;
import cn.poverty.interaction.req.department.DepartmentUpdateReq;
import cn.poverty.interaction.resp.department.AuthDepartmentTreeResp;
import cn.poverty.interaction.resp.department.AuthDepartmentTreeWrapResp;
import cn.poverty.repository.entity.AuthDepartment;
import cn.poverty.repository.repository.AuthDepartmentRepository;
import cn.poverty.repository.repository.AuthMenuRepository;
import cn.poverty.repository.repository.AuthPermissionRepository;
import cn.poverty.repository.repository.AuthRoleRepository;
import cn.poverty.repository.repository.AuthUserConfigRepository;
import cn.poverty.repository.repository.AuthUserRepository;
import cn.poverty.repository.repository.AuthUserRoleRepository;
import cn.poverty.repository.repository.SystemLoginLogRepository;
import cn.poverty.repository.result.AuthDepartmentResult;
import cn.poverty.service.AuthCacheService;
import cn.poverty.service.AuthDepartmentService;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: ??????
 * @date 2019-08-23
 */
@Service(value = "authDepartmentService")
@Slf4j
public class AuthDepartmentImpl implements AuthDepartmentService {

    @Resource
    private AuthUserRepository authUserRepository;

    @Resource
    private AuthDepartmentRepository authDepartmentRepository;

    @Resource
    private AuthPermissionRepository authPermissionRepository;

    @Resource
    private AuthRoleRepository authRoleRepository;

    @Resource
    private AuthUserRoleRepository authUserRoleRepository;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthCacheService authCacheService;

    @Resource
    private SystemLoginLogRepository systemLoginLogRepository;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private AuthUserConfigRepository authUserConfigRepository;

    @Resource
    private AuthMenuRepository authMenuRepository;



    /**
      * ????????????
      *
      * @date 2019-08-30
      * @param departmentAddReq ????????????
      */
    @Override
    public void addAuthDepartment(DepartmentAddReq departmentAddReq){

        AuthDepartment authDepartment = new AuthDepartment();

         String parentId = departmentAddReq.getParentId();

        if(CheckParam.isNull(parentId)) {
            authDepartment.setParentId("0");
        }
        if(!CheckParam.isNull(departmentAddReq.getDepartmentName())){
            authDepartment.setDepartmentName(departmentAddReq.getDepartmentName());
        }
        if(!CheckParam.isNull(departmentAddReq.getOrderNum())){
            authDepartment.setOrderNum(departmentAddReq.getOrderNum());
        }
        if(!CheckParam.isNull(departmentAddReq.getParentId())){
            authDepartment.setParentId(departmentAddReq.getParentId());
        }
        authDepartmentRepository.insert(authDepartment);
    }


    /**
     * ??????????????????
     *
     * @date 2019-08-30
     * @param departmentUpdateReq ????????????req
     */
    @Override
    public void updateDepartment(DepartmentUpdateReq departmentUpdateReq){

        Example  authDepartmentExample = Example.builder(AuthDepartment.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("departmentId", departmentUpdateReq.getDepartmentId()))
                .build();

        AuthDepartment authDepartment = authDepartmentRepository.selectOneByExample(authDepartmentExample);

        if(CheckParam.isNull(authDepartment)) {
            return;
        }

        if(!CheckParam.isNull(departmentUpdateReq.getDepartmentName())){
            authDepartment.setDepartmentName(departmentUpdateReq.getDepartmentName());
        }
        if(!CheckParam.isNull(departmentUpdateReq.getOrderNum())){
            authDepartment.setOrderNum(departmentUpdateReq.getOrderNum());
        }
        if(!CheckParam.isNull(departmentUpdateReq.getParentId())){
            authDepartment.setParentId(departmentUpdateReq.getParentId());
        }

        authDepartment.setUpdateTime(LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone()));

        authDepartmentRepository.updateByPrimaryKeySelective(authDepartment);
    }


    /**
     * ????????????
     *
     * @date 2019-09-05
     * @param departmentIdList ?????????????????????????????????ID
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteDepartment(List<String> departmentIdList){
        List<AuthDepartmentResult> departmentResultList =
                authDepartmentRepository.queryAllDepartment(baseConstant.getUnDeleteStatus());

        Map<String, List<AuthDepartmentResult>> authDepartmentHashMap = departmentResultList.stream()
                .collect(Collectors.groupingBy(AuthDepartmentResult::getParentId));

        ArrayList<String> authDepartmentIdList = Lists.newArrayList();

        //???????????????????????????
        departmentIdList.stream().forEach(item -> {
            handleParentId(authDepartmentIdList,authDepartmentHashMap,item);
        });

        if(!authDepartmentIdList.isEmpty()){
            authDepartmentRepository.updateDeleteStatusByDepartmentIdList(baseConstant.getDeleteStatus(),authDepartmentIdList);
        }
    }

    /**
     *
     * @date 2021/4/18
     * @param departmentIdList ??????????????????ID???ID
     * @param authDepartmentHashMap
     * @param departmentId ??????ID
     */
    public void handleParentId(List<String> departmentIdList,
                               Map<String, List<AuthDepartmentResult>> authDepartmentHashMap,
                               String departmentId){
        List<AuthDepartmentResult> authDepartmentList = authDepartmentHashMap.get(departmentId);

        if(!CollectionUtils.isEmpty(authDepartmentList)){
            List<String> needHandleIdList =
                    authDepartmentList.stream().map(item -> item.getDepartmentId()).distinct().collect(Collectors.toList());
            departmentIdList.addAll(needHandleIdList);
            needHandleIdList.stream().forEach(item -> {
                handleParentId(departmentIdList,authDepartmentHashMap,item);
            });
        }
        if(!CollectionUtil.contains(departmentIdList,departmentId)){
            departmentIdList.add(departmentId);
        }
    }

    /**
      * ????????????????????????????????????
      *
      * @date 2019-08-23
      * @param queryReq ??????????????????req
      * @return AuthDepartmentTreeWrapResp
      */
    @Override
    public AuthDepartmentTreeWrapResp queryAllDepartmentTreeList(DepartmentQueryReq queryReq){

        //??????????????????
        Example pageExample = new Example(AuthDepartment.class);
        Example.Criteria pageCriteria = pageExample.createCriteria();
        CriteriaBuilder.rigidCriteria(pageCriteria);

        if(!CheckParam.isNull(queryReq.getDepartmentName())){
            pageCriteria.andLike("departmentName","%"+ queryReq.getDepartmentName()+"%");
        }

        if(!CheckParam.isNull(queryReq.getBeginTime())){
            pageCriteria.andGreaterThanOrEqualTo("createTime",queryReq.getBeginTime());
        }

        if(!CheckParam.isNull(queryReq.getEndTime())){
            pageCriteria.andLessThanOrEqualTo("createTime",queryReq.getEndTime());
        }

        pageExample.orderBy("createTime").desc();

        //????????????
        List<AuthDepartment> entityList = authDepartmentRepository.selectByExample(pageExample);

        if(CollectionUtils.isEmpty(entityList)) {
            return new AuthDepartmentTreeWrapResp();
        }

        List<AuthDepartmentTreeResp> authDepartmentRespList = new ArrayList<>();

        entityList.forEach(u1 -> {
            AuthDepartmentTreeResp treeResp = new AuthDepartmentTreeResp();

            treeResp.setCreateTime(u1.getCreateTime());
            treeResp.setDepartmentId(u1.getDepartmentId());
            treeResp.setDepartmentName(u1.getDepartmentName());
            treeResp.setOrderNum(u1.getOrderNum());
            treeResp.setParentId(u1.getParentId());
            treeResp.setCreateTime(u1.getCreateTime());
            treeResp.setUpdateTime(u1.getUpdateTime());

            authDepartmentRespList.add(treeResp);
        });

        //?????????????????????
        List<Tree<AuthDepartmentTreeResp>> treeRespList = new ArrayList<>();

        //????????????????????????????????????
        buildTrees(treeRespList, authDepartmentRespList);

        AuthDepartmentTreeWrapResp resp = new AuthDepartmentTreeWrapResp();

        Tree<AuthDepartmentTreeResp> departmentTree = TreeUtil.build(treeRespList);

        resp.setTrees(departmentTree);
        resp.setTotal(treeRespList.size());

        //?????????????????????????????????????????????
        return resp;
    }

    /**
      * ???????????????
      *
      * @date 2019-08-23
      * @param trees  ??????????????????????????????List
      * @param authDepartmentList  ????????????
      */
    private void buildTrees(List<Tree<AuthDepartmentTreeResp>> trees, List<AuthDepartmentTreeResp> authDepartmentList) {
        authDepartmentList.forEach(dept -> {
            Tree<AuthDepartmentTreeResp> tree = new Tree<>();

            tree.setId(dept.getDepartmentId());
            tree.setKey(tree.getId());
            tree.setParentId(dept.getParentId());
            tree.setCreateTime(dept.getCreateTime());
            tree.setUpdateTime(dept.getUpdateTime());
            tree.setOrder(dept.getOrderNum());
            tree.setText(dept.getDepartmentName());
            tree.setTitle(tree.getText());
            tree.setValue(tree.getId());
            trees.add(tree);
        });
    }


    /**
     * ????????????????????????????????????
     *
     * @date 2019-08-23
     * @return Tree
     */
    @Override
    public Tree<AuthDepartmentTreeResp> queryAllDepartmentTree(){

        Example authDepartmentExample = Example.builder(AuthDepartment.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus()))
                .build();

        List<AuthDepartment> authDepartmentList = authDepartmentRepository.selectByExample(authDepartmentExample);

        if(CollectionUtils.isEmpty(authDepartmentList)) {
            return new Tree<>();
        }

        List<AuthDepartmentTreeResp> authDepartmentRespList = new ArrayList<>();

        authDepartmentList.forEach(u1 -> {
            AuthDepartmentTreeResp treeResp = new AuthDepartmentTreeResp();

            treeResp.setCreateTime(u1.getCreateTime());
            treeResp.setDepartmentId(u1.getDepartmentId());
            treeResp.setDepartmentName(u1.getDepartmentName());
            treeResp.setOrderNum(u1.getOrderNum());
            treeResp.setParentId(u1.getParentId());

            authDepartmentRespList.add(treeResp);
        });

        //?????????????????????
        List<Tree<AuthDepartmentTreeResp>> treeRespList = new ArrayList<>();

        //????????????????????????????????????
        buildTrees(treeRespList, authDepartmentRespList);


        //?????????????????????????????????????????????
        return TreeUtil.build(treeRespList);
    }


}
