package cn.poverty.service.impl;

import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.constants.PlatformConstant;
import cn.poverty.common.entity.Tree;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.CriteriaBuilder;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.common.TreeUtil;
import cn.poverty.interaction.req.menu.AuthMenuAddReq;
import cn.poverty.interaction.req.menu.AuthMenuTreeQueryReq;
import cn.poverty.interaction.req.menu.AuthMenuUpdateReq;
import cn.poverty.interaction.resp.menu.AuthMenuTreeQueryWrapResp;
import cn.poverty.interaction.resp.menu.AuthMenuTreeResp;
import cn.poverty.repository.entity.AuthMenu;
import cn.poverty.repository.repository.AuthMenuRepository;
import cn.poverty.repository.repository.AuthPermissionRepository;
import cn.poverty.repository.repository.AuthRoleMenuRepository;
import cn.poverty.repository.repository.AuthRoleRepository;
import cn.poverty.repository.repository.AuthUserConfigRepository;
import cn.poverty.repository.repository.AuthUserRepository;
import cn.poverty.repository.repository.AuthUserRoleRepository;
import cn.poverty.repository.repository.SystemLoginLogRepository;
import cn.poverty.repository.result.AuthMenuResult;
import cn.poverty.service.AuthCacheService;
import cn.poverty.service.AuthMenuService;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统菜单Service方法实现
 * @date 2019-08-26
 */
@Service(value = "authMenuService")
@Slf4j
public class AuthMenuServiceImpl implements AuthMenuService {

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


    /**
     * 更新菜单
     * @author
     * @date 2019-09-05
     * @param authMenuUpdateReq 更新菜单Req
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateMenu(AuthMenuUpdateReq authMenuUpdateReq){
        Example  authMenuExample = Example.builder(AuthMenu.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("authMenuId", authMenuUpdateReq.getAuthMenuId()))
                .build();
        AuthMenu authMenu = authMenuRepository.selectOneByExample(authMenuExample);
        if(CheckParam.isNull(authMenu)) {
            return;
        }
        if(!CheckParam.isNull(authMenuUpdateReq.getMenuName())){
            authMenu.setMenuName(authMenuUpdateReq.getMenuName());
        }
        if(!CheckParam.isNull(authMenuUpdateReq.getOrderNum())){
            authMenu.setOrderNum(authMenuUpdateReq.getOrderNum());
        }
        if(!CheckParam.isNull(authMenuUpdateReq.getPerms())){
            authMenu.setPerms(authMenuUpdateReq.getPerms());
        }

        if(!CheckParam.isNull(authMenuUpdateReq.getPath())){
            authMenu.setPath(authMenuUpdateReq.getPath());
        }
        if(!CheckParam.isNull(authMenuUpdateReq.getComponent())){
            authMenu.setComponent(authMenuUpdateReq.getComponent());
        }
        if(!CheckParam.isNull(authMenuUpdateReq.getMenuType())){
            authMenu.setMenuType(authMenuUpdateReq.getMenuType());
        }
        if(!CheckParam.isNull(authMenuUpdateReq.getParentId())){
            authMenu.setParentId(authMenuUpdateReq.getParentId());
        }
        if(!CheckParam.isNull(authMenuUpdateReq.getMenuIcon())){
            authMenu.setMenuIcon(authMenuUpdateReq.getMenuIcon());
        }
        if (CheckParam.isNull(authMenuUpdateReq.getParentId())) {
            authMenu.setParentId("0");
        }
        if (PlatformConstant.TYPE_BUTTON.equals(authMenuUpdateReq.getMenuType())) {
            authMenu.setPath(null);
            authMenu.setMenuIcon(null);
            authMenu.setComponent(null);
        }
        authMenuRepository.updateByPrimaryKeySelective(authMenu);
        List<String> authMenuUserIdList = authMenuRepository.queryAuthUserIdByAuthMenuId(baseConstant.getUnDeleteStatus(), authMenu.getAuthMenuId());
        if(!CheckParam.isNull(authMenuUserIdList) && !authMenuUserIdList.isEmpty()){
            authCacheService.saveUserPermissionRoleRedisCache(authMenuUserIdList);
        }
    }

    /**
     * 删除菜单
     * @author
     * @date 2019-09-05
     * @param authMenuIdList 需要被删除的菜单的主键ID
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteMenu(List<String> authMenuIdList){
        List<AuthMenuResult> authMenuResultList = authMenuRepository.queryAllMenu(baseConstant.getUnDeleteStatus());
        Map<String, List<AuthMenuResult>> authMenuHashMap = authMenuResultList.stream()
                .collect(Collectors.groupingBy(AuthMenuResult::getParentId));
        ArrayList<String> menuIdList = Lists.newArrayList();
        //处理菜单级联的问题
        authMenuIdList.stream().forEach(item -> {
            handleParentId(menuIdList,authMenuHashMap,item);
        });
        if(!menuIdList.isEmpty()){
            authMenuRepository.updateDeleteStatusByAuthMenuIdListIn(baseConstant.getDeleteStatus(),menuIdList);
            authRoleMenuRepository.deleteByMenuIdList(menuIdList);
        }
    }


    /**
     * @author
     * @date 2021/4/18
     * @param menuIdList 需要装载菜单ID的ID
     * @param authMenuHashMap
     * @param authMenuId 菜单ID
     */
    public void handleParentId(List<String> menuIdList,
                               Map<String, List<AuthMenuResult>> authMenuHashMap,
                               String authMenuId){
        List<AuthMenuResult> menuResultList = authMenuHashMap.get(authMenuId);

        if(!CollectionUtils.isEmpty(menuResultList)){
            List<String> needHandleIdList =
                    menuResultList.stream().map(item -> item.getAuthMenuId()).distinct().collect(Collectors.toList());
            menuIdList.addAll(needHandleIdList);
            needHandleIdList.stream().forEach(item -> {
                handleParentId(menuIdList,authMenuHashMap,item);
            });
        }
        if(!CollectionUtil.contains(menuIdList,authMenuId)){
            menuIdList.add(authMenuId);
        }
    }

    /**
     * 新增菜单
     * @author
     * @date 2019-09-05
     * @param authMenuAddReq 新增菜单请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addMenu(AuthMenuAddReq authMenuAddReq){
        AuthMenu authMenu = new AuthMenu();
        authMenu.setMenuName(authMenuAddReq.getMenuName());
        authMenu.setOrderNum(authMenuAddReq.getOrderNum());
        authMenu.setPerms(authMenuAddReq.getPerms());
        authMenu.setPath(authMenuAddReq.getPath());
        authMenu.setComponent(authMenuAddReq.getComponent());
        authMenu.setMenuType(authMenuAddReq.getMenuType());
        authMenu.setParentId(authMenuAddReq.getParentId());
        authMenu.setMenuIcon(authMenuAddReq.getMenuIcon());
        if (CheckParam.isNull(authMenuAddReq.getParentId())) {
            authMenu.setParentId("0");
        }
        if (PlatformConstant.TYPE_BUTTON.equals(authMenuAddReq.getMenuType())) {
            authMenu.setPath(null);
            authMenu.setMenuIcon(null);
            authMenu.setComponent(null);
        }
        authMenuRepository.insert(authMenu);

    }




    /**
     * 权限菜单树集合查询
     * @author
     * @date 2019-08-26
     * @param authMenuTreeQueryReq 权限菜单树查询Req
     * @return AuthMenuTreeListQueryWrapResp
     */
    /*@Override
    public AuthMenuTreeListQueryWrapResp queryMenuTreeList(AuthMenuTreeQueryReq authMenuTreeQueryReq){
        //构建查询条件
        Example authMenuExample = new Example(AuthMenu.class);
        Example.Criteria authMenuCriteria = authMenuExample.createCriteria();
        CriteriaBuilder.rigidCriteria(authMenuCriteria);

        if(!CheckParam.isNull(authMenuTreeQueryReq.getMenuName())){
            authMenuCriteria.andEqualTo("menuName",authMenuTreeQueryReq.getMenuName());
        }

        if(!CheckParam.isNull(authMenuTreeQueryReq.getMenuType())){
            authMenuCriteria.andEqualTo("menuType",authMenuTreeQueryReq.getMenuType());
        }

        List<AuthMenu> authMenuList = new ArrayList<>();

        authMenuList.addAll(authMenuRepository.selectByExample(authMenuExample));
        AuthMenuTreeListQueryWrapResp resp = new AuthMenuTreeListQueryWrapResp();

        //用于格式化数据的菜单树
        List<AuthMenuQueryTreeResp> respList = new ArrayList<>();

        List<Tree<AuthMenuQueryTreeResp>> trees = new ArrayList<>();

        resp.setTotal(authMenuList.size());
        resp.setIds(authMenuList.stream().map(r1 -> r1.getAuthMenuId()).collect(Collectors.toList()));

        authMenuList.stream().forEach(u1 -> {
            AuthMenuQueryTreeResp treeResp = new AuthMenuQueryTreeResp();

            treeResp.setUpdateTime(u1.getUpdateTime());
            treeResp.setAuthMenuId(u1.getAuthMenuId());
            treeResp.setPerms(u1.getPerms());
            treeResp.setComponent(u1.getComponent());
            treeResp.setMenuIcon(u1.getMenuIcon());
            treeResp.setMenuType(u1.getMenuType());
            treeResp.setOrderNum(u1.getOrderNum());
            treeResp.setMenuName(u1.getMenuName());
            treeResp.setPath(u1.getPath());
            treeResp.setParentId(u1.getParentId());
            treeResp.setCreateTime(u1.getCreateTime());

            respList.add(treeResp);
        });

        //构建菜单树
        buildTrees(trees,respList);
        if (authMenuTreeQueryReq.getNeedAll()){
            Tree<AuthMenuQueryTreeResp> menuTree = TreeUtil.build(trees);
            resp.setMenuTree(menuTree);
        }else {
            resp.setTrees(trees);
        }
        return resp;
    }*/



    /**
     * 构建菜单树
     * @author
     * @date 2019-08-26
     * @param trees  菜单树Resp集合
     * @param itemList  菜单集合
     * @return
     */
    private void buildTrees(List<Tree<AuthMenuTreeResp>> trees,
                            List<AuthMenuTreeResp> itemList) {
        itemList.forEach(item -> {
            Tree<AuthMenuTreeResp> tree = new Tree<>();
            tree.setId(item.getAuthMenuId());
            tree.setKey(tree.getId());
            tree.setParentId(item.getParentId());
            tree.setIcon(item.getMenuIcon());
            tree.setComponent(item.getComponent());
            tree.setPath(item.getPath());
            tree.setOrder(item.getOrderNum());
            tree.setPermission(item.getPerms());
            tree.setType(item.getMenuType());
            tree.setText(item.getMenuName());
            tree.setTitle(tree.getText());
            tree.setValue(tree.getId());
            tree.setCreateTime(item.getCreateTime());
            tree.setUpdateTime(item.getUpdateTime());
            trees.add(tree);
        });
    }


    /**
     * 查询所有部门树集合
     * @author
     * @date 2019-08-26
     * @param authMenuTreeQueryReq
     * @return AuthMenuTreeQueryWrapResp
     */
    @Override
    public AuthMenuTreeQueryWrapResp queryAllAuthMenuTreeList(AuthMenuTreeQueryReq authMenuTreeQueryReq){
        //构建查询条件
        Example authMenuExample = new Example(AuthMenu.class);
        Example.Criteria authMenuCriteria = authMenuExample.createCriteria();
        CriteriaBuilder.rigidCriteria(authMenuCriteria);
        if(!CheckParam.isNull(authMenuTreeQueryReq.getMenuName())){
            authMenuCriteria.andEqualTo("menuName",authMenuTreeQueryReq.getMenuName());
        }
        if(!CheckParam.isNull(authMenuTreeQueryReq.getMenuType())){
            authMenuCriteria.andEqualTo("menuType",authMenuTreeQueryReq.getMenuType());
        }
        List<AuthMenu> authMenuList = authMenuRepository.selectByExample(authMenuExample);
        AuthMenuTreeQueryWrapResp resp = new AuthMenuTreeQueryWrapResp();
        //用于格式化数据的菜单树
        List<AuthMenuTreeResp> respList = new ArrayList<>();
        List<Tree<AuthMenuTreeResp>> trees = new ArrayList<>();
        resp.setTotal(authMenuList.size());
        resp.setIds(authMenuList.stream().map(r1 -> String.valueOf(r1.getId())).collect(Collectors.toList()));
        authMenuList.stream().forEach(u1 -> {
            AuthMenuTreeResp treeResp = new AuthMenuTreeResp();
            treeResp.setAuthMenuId(u1.getAuthMenuId());
            treeResp.setComponent(u1.getComponent());
            treeResp.setMenuIcon(u1.getMenuIcon());
            treeResp.setMenuName(u1.getMenuName());
            treeResp.setMenuType(u1.getMenuType());
            treeResp.setOrderNum(u1.getOrderNum());
            treeResp.setParentId(u1.getParentId());
            treeResp.setPath(u1.getPath());
            treeResp.setPerms(u1.getPerms());
            treeResp.setCreateTime(u1.getCreateTime());
            treeResp.setUpdateTime(u1.getUpdateTime());
            respList.add(treeResp);
        });
        //构建菜单树
        buildTrees(trees,respList);
        Tree<AuthMenuTreeResp> buildTreeResult = TreeUtil.build(trees);
        resp.setMenuTree(buildTreeResult);
        return resp;
    }

    /**
     * 查询部门树
     * @author
     * @date 2019-08-26
     * @param authMenuTreeQueryReq
     * @return AuthMenuTreeQueryWrapResp
     */
    @Override
    public Tree<AuthMenuTreeResp> queryAllAuthMenuTree(AuthMenuTreeQueryReq authMenuTreeQueryReq){
        //构建查询条件
        Example authMenuExample = new Example(AuthMenu.class);
        Example.Criteria authMenuCriteria = authMenuExample.createCriteria();
        CriteriaBuilder.rigidCriteria(authMenuCriteria);

        if(!CheckParam.isNull(authMenuTreeQueryReq.getMenuName())){
            authMenuCriteria.andEqualTo("menuName",authMenuTreeQueryReq.getMenuName());
        }

        if(!CheckParam.isNull(authMenuTreeQueryReq.getMenuType())){
            authMenuCriteria.andEqualTo("menuType",authMenuTreeQueryReq.getMenuType());
        }

        List<AuthMenu> authMenuList = authMenuRepository.selectByExample(authMenuExample);

        //AuthMenuTreeQueryWrapResp resp = new AuthMenuTreeQueryWrapResp();

        //用于格式化数据的菜单树
        List<AuthMenuTreeResp> respList = new ArrayList<>();

        List<Tree<AuthMenuTreeResp>> trees = new ArrayList<>();

        //resp.setTotal(authMenuList.size());
        //resp.setIds(authMenuList.stream().map(r1 -> String.valueOf(r1.getId())).collect(Collectors.toList()));

        authMenuList.stream().forEach(u1 -> {
            AuthMenuTreeResp treeResp = new AuthMenuTreeResp();

            treeResp.setAuthMenuId(u1.getAuthMenuId());
            treeResp.setComponent(u1.getComponent());
            treeResp.setMenuIcon(u1.getMenuIcon());
            treeResp.setMenuName(u1.getMenuName());
            treeResp.setMenuType(u1.getMenuType());
            treeResp.setOrderNum(u1.getOrderNum());
            treeResp.setParentId(u1.getParentId());
            treeResp.setPath(u1.getPath());
            treeResp.setPerms(u1.getPerms());
            treeResp.setCreateTime(u1.getCreateTime());
            treeResp.setUpdateTime(u1.getUpdateTime());

            respList.add(treeResp);
        });

        //构建菜单树
        buildTrees(trees,respList);

        Tree<AuthMenuTreeResp> tree = TreeUtil.build(trees);

        //resp.setMenuTree(buildTreeResult);
        //return resp;
        return tree;
    }

}
