package cn.poverty.service.impl;

import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.enums.ErrorCode;
import cn.poverty.common.exception.BusinessException;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.CriteriaBuilder;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.interaction.helper.PageBuilder;
import cn.poverty.interaction.req.role.AuthRoleAddReq;
import cn.poverty.interaction.req.role.AuthRolePageReq;
import cn.poverty.interaction.req.role.AuthRoleUpdateReq;
import cn.poverty.interaction.resp.page.Pagination;
import cn.poverty.interaction.resp.role.AuthRolePageQueryResp;
import cn.poverty.repository.entity.AuthRole;
import cn.poverty.repository.entity.AuthRoleMenu;
import cn.poverty.repository.repository.AuthMenuRepository;
import cn.poverty.repository.repository.AuthPermissionRepository;
import cn.poverty.repository.repository.AuthRoleMenuRepository;
import cn.poverty.repository.repository.AuthRoleRepository;
import cn.poverty.repository.repository.AuthUserConfigRepository;
import cn.poverty.repository.repository.AuthUserRepository;
import cn.poverty.repository.repository.AuthUserRoleRepository;
import cn.poverty.repository.repository.SystemLoginLogRepository;
import cn.poverty.service.AuthCacheService;
import cn.poverty.service.AuthRoleService;
import cn.hutool.core.util.StrUtil;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: ??????????????????????????????
 * @date 2019-08-23
 */
@Service(value = "authRoleService")
@Slf4j
public class AuthRoleServiceImpl implements AuthRoleService {

    @Resource
    private AuthUserRepository authUserRepository;

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

    @Resource
    private AuthRoleMenuRepository authRoleMenuRepository;

    @Resource
    private MapperFacade mapperFacade;


    /**
      * ????????????????????????
      *
      * @date 2019-08-23
      * @param authRolePageReq ??????????????????????????????
      * @return Pagination
      */
    @Override
    public Pagination<AuthRolePageQueryResp> queryAuthRoleByPage(AuthRolePageReq authRolePageReq){

        //??????????????????
        Example pageExample = new Example(AuthRole.class);
        Example.Criteria authRoleCriteria = pageExample.createCriteria();
        CriteriaBuilder.rigidCriteria(authRoleCriteria);


        if(!CheckParam.isNull(authRolePageReq.getRoleName())){
            authRoleCriteria.andEqualTo("roleName", authRolePageReq.getRoleName());
        }
        if(!CheckParam.isNull(authRolePageReq.getOptionalStatus())){
            authRoleCriteria.andEqualTo("optionalStatus", authRolePageReq.getOptionalStatus());
        }
        if(!CheckParam.isNull((authRolePageReq.getBeginTime()))){
            authRoleCriteria.andGreaterThanOrEqualTo("createTime", authRolePageReq.getBeginTime());
        }
        if(!CheckParam.isNull((authRolePageReq.getEndTime()))){
            authRoleCriteria.andLessThanOrEqualTo("createTime", authRolePageReq.getEndTime());
        }

        pageExample.orderBy("createTime").desc();

        List<AuthRole> authRoleList = new ArrayList<>();
        Page<Object> page = new Page();

        if(authRolePageReq.getNeedAll()){
            authRoleList.addAll(authRoleRepository.selectByExample(pageExample));
            page.setTotal(authRoleList.size());
            page.setPageSize(authRoleList.size());
            page.setPageNum(1);
            page.setPages(1);

        }else{
            //????????????
            page = PageHelper.startPage(authRolePageReq.getCurrentPage(), authRolePageReq.getItemsPerPage());
            authRoleList.addAll(authRoleRepository.selectByExample(pageExample));
        }

        if(CheckParam.isNull(authRoleList) || authRoleList.isEmpty()) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }

        List<AuthRolePageQueryResp> authRoleRespList = new ArrayList<>();

        authRoleList.stream().forEach(u1 -> {

            AuthRolePageQueryResp resp = new AuthRolePageQueryResp();

            resp.setCreateTime(u1.getCreateTime());
            resp.setUpdateTime(u1.getUpdateTime());
            resp.setRemark(u1.getRemark());
            resp.setAuthRoleId(u1.getAuthRoleId());
            resp.setRoleName(u1.getRoleName());
            resp.setRoleCode(u1.getRoleCode());
            resp.setOptionalStatus(u1.getOptionalStatus());

            authRoleRespList.add(resp);
        });

        return PageBuilder.buildPageResult(page,authRoleRespList);

    }

    /**
      * ??????????????????---?????????

      * @date 2019/9/2
      * @return List
      */
    @Override
    public List<AuthRolePageQueryResp> queryAllRoles() {
        //???????????????
        Example  roleExample = Example.builder(AuthRole.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus()))
                .build();
        List<AuthRole> authRoleList = authRoleRepository.selectByExample(roleExample);
        List<AuthRolePageQueryResp> respList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(authRoleList)){
            return mapperFacade.mapAsList(authRoleList,AuthRolePageQueryResp.class);
        }
        return respList;
    }

    /**
      * ??????????????????????????????

      * @date 2019/9/4
      * @param authRoleId ??????ID
      * @return List
      */
    @Override
    public List<String> queryMenuIdList(String authRoleId) {
        log.info(">>>>>>>>>>>>>>>>?????????????????????????????????,??????:{}<<<<<<<<<<<<<<<<<",authRoleId);
        //??????
        Example authRoleMenuExample = Example.builder(AuthRoleMenu.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("authRoleId", authRoleId))
                .build();

        List<AuthRoleMenu> roleMenuList = authRoleMenuRepository.selectByExample(authRoleMenuExample);

        if(CollectionUtils.isEmpty(roleMenuList)){
            return Collections.EMPTY_LIST;
        }

        List<String> authMenuIdList = roleMenuList.stream().map(item -> item.getMenuId()).collect(Collectors.toList());
        return authMenuIdList;
    }

    /**
     * ????????????
     *
     * @date 2021/3/30
     * @param updateReq ??????????????????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(AuthRoleUpdateReq updateReq) {
        log.info(">>>>>>>>>>>>>??????????????????,??????:{}<<<<<<<<<<<<<<<<",updateReq.toString());
        String roleCode = updateReq.getRoleCode();
        String optionalStatus = updateReq.getOptionalStatus();
        //??????
        Example  roleExample = Example.builder(AuthRole.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("authRoleId", updateReq.getAuthRoleId()))
                .build();
        AuthRole authRole = authRoleRepository.selectOneByExample(roleExample);
        if (!CheckParam.isNull(optionalStatus)){
            authRole.setOptionalStatus(optionalStatus);
        }
        //??????
        if (!CheckParam.isNull(updateReq.getRemark())&&!authRole.getRemark().equalsIgnoreCase(updateReq.getRemark())){
            authRole.setRemark(updateReq.getRemark());
        }
        if (!CheckParam.isNull(roleCode)){
            //????????????????????????????????????
            if(!StrUtil.equalsAnyIgnoreCase(authRole.getRoleCode(),roleCode)){
                authRole.setRoleCode(roleCode);
            }
        }
        //??????
        authRoleRepository.updateByPrimaryKey(authRole);
        //???????????????????????????
        Example  roleMenuExample = Example.builder(AuthRoleMenu.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("authRoleId", updateReq.getAuthRoleId()))
                .build();
        List<AuthRoleMenu> menus = authRoleMenuRepository.selectByExample(roleMenuExample);
        //???????????????menuId
        List<String> oldMenuIds = menus.stream().map(menu -> menu.getMenuId()).collect(Collectors.toList());
        //?????????set
        Set<String> oldSet = new HashSet<>(oldMenuIds);
        //???????????????
        List<String> newMenuIdList = updateReq.getAuthMenuIdList();
        //?????????set
        Set<String> newSet = new HashSet<>(newMenuIdList);
        //?????????????????????
        Set<String> flagSet = new HashSet<>();
        flagSet.addAll(oldSet);
        flagSet.removeAll(newSet);
        log.info(">>>>>>>>>>>>>>>???????????????:{}->???????????????????????????:{} <<<<<<<<<",updateReq.getAuthRoleId(),flagSet.toString());
        if (!CheckParam.isNull(flagSet) && flagSet.size() > 0){
            flagSet.stream().forEach(menuId->{
                authRoleMenuRepository.deleteByRoleAndMenuId(updateReq.getAuthRoleId(),menuId);
            });
        }
        //?????????????????????
        flagSet.clear();
        flagSet.addAll(newSet);
        flagSet.removeAll(oldSet);
        log.info(">>>>>>>>>>>>>>>???????????????:{}->???????????????????????????:{}",updateReq.getAuthRoleId(),flagSet.toString());
        if (!CheckParam.isNull(flagSet) && flagSet.size() > 0){
            flagSet.stream().forEach(menuId->{
                AuthRoleMenu authRoleMenu = new AuthRoleMenu();
                authRoleMenu.setAuthRoleId(updateReq.getAuthRoleId());
                authRoleMenu.setMenuId(menuId);
                authRoleMenuRepository.insert(authRoleMenu);
            });
        }
    }

    /**
      * ????????????

      * @date 2019/9/4
      * @param authRoleIdList
      */
    @Override
    public void batchDeleteItem(String authRoleIdList) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>??????????????????,??????:{}<<<<<<<<<<<<<<<<<<<<<<<<",authRoleIdList);
        List<String> roleIdList = Arrays.asList(authRoleIdList.split(","));
        if (!CheckParam.isNull(roleIdList)){
            roleIdList.stream().forEach(authRoleId->{
                authRoleRepository.deleteByRoleId(authRoleId);
            });
            authRoleMenuRepository.deleteByAuthRoleIdList(roleIdList);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @date 2021/5/21
     * @param roleName ????????????
     * @return Boolean
     */
    @Override
    public Boolean roleNameExisted(String roleName){
        Example  authRoleExample = Example.builder(AuthRole.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("roleName", roleName))
                .build();

        Integer roleCount = authRoleRepository.selectCountByExample(authRoleExample);

        if(roleCount.compareTo(BigInteger.ZERO.intValue()) != 0){
            return true;
        }else{
            return false;
        }
    }

    /**
      * ??????????????????????????????
      *
      * @date 2021/5/21
      * @param roleCode ????????????
      * @return Boolean
      */
    @Override
    public Boolean roleCodeExisted(String roleCode){
        Example  authRoleExample = Example.builder(AuthRole.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("roleCode", roleCode))
                .build();

        Integer roleCount = authRoleRepository.selectCountByExample(authRoleExample);

        if(roleCount.compareTo(BigInteger.ZERO.intValue()) != 0){
            return true;
        }else{
            return false;
        }
    }

    /**
      * ??????????????????????????????
      *
      * @date 2021/4/27
      * @param roleCode ????????????
      */
    @Override
    public void verifyRoleCode(String roleCode){

        if(CheckParam.isNull(roleCode)){
            return;
        }

        Example  authRoleExample = Example.builder(AuthRole.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("roleCode", roleCode))
                .build();

        Integer roleCount = authRoleRepository.selectCountByExample(authRoleExample);

        if(roleCount.compareTo(BigInteger.ZERO.intValue()) != 0){
            throw new BusinessException(ErrorCode.REPEAT_ROLE_CODE_ERROR.getCode(),
                    ErrorCode.REPEAT_ROLE_CODE_ERROR.getMessage());
        }
    }

    /**
     * ????????????
     *
     * @date 2021/3/30
     * @param addReq ????????????????????????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(AuthRoleAddReq addReq) {
        log.info(">>>>>>>>>>>>>>>>>????????????????????????: {} <<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String roleCode = addReq.getRoleCode();
        verifyRoleCode(roleCode);
        //????????????
        AuthRole authRole = new AuthRole();
        authRole.setRemark(addReq.getRemark());
        authRole.setRoleName(addReq.getRoleName());
        authRole.setRoleCode(roleCode);
        authRole.setOptionalStatus(addReq.getOptionalStatus());
        authRoleRepository.insert(authRole);

        List<String> authMenuIdList = addReq.getAuthMenuIdList();
        //???????????????????????????
        if (!CollectionUtils.isEmpty(authMenuIdList)){
            List<AuthRoleMenu> authRoleMenuList = new ArrayList<>();

            authMenuIdList.stream().forEach(authMenuId->{
                AuthRoleMenu authRoleMenu = new AuthRoleMenu();
                authRoleMenu.setMenuId(authMenuId);
                authRoleMenu.setAuthRoleId(authRole.getAuthRoleId());

                authRoleMenuList.add(authRoleMenu);
            });

            if(!authRoleMenuList.isEmpty()){
                authRoleMenuRepository.insertList(authRoleMenuList);
            }
        }
    }


}
