package cn.poverty.common.utils.common;

import cn.poverty.common.entity.RouterMeta;
import cn.poverty.common.entity.Tree;
import cn.poverty.common.entity.VueRouter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统树工具
 * @date 2019-08-22
 */
@Slf4j
public class TreeUtil {

    protected TreeUtil() {

    }

    /**
     * 顶级的NODE的ID
     */
    private final static String TOP_NODE_ID = "0";

    /**
     * 用于构建菜单或部门树
     *
     * @param nodes nodes
     * @param <T>   <T>
     * @return <T> Tree<T>
     */
    public static <T> Tree<T> build(List<Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            String parentId = node.getParentId();
            if (parentId == null || TOP_NODE_ID.equals(parentId)) {
                topNodes.add(node);
                return;
            }
            for (Tree<T> n : nodes) {
                String id = n.getId();
                if (id != null && id.equals(parentId)) {
                    if (n.getChildren() == null) {
                        n.initChildren();
                    }
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty()) {
                topNodes.add(node);
            }
        });
        Tree<T> root = new Tree<>();
        root.setId("0");
        root.setParentId("");
        root.setHasParent(false);
        root.setHasChildren(true);
        root.setChildren(topNodes);
        root.setText("root");
        return root;
    }


    /**
     * 构造前端路由
     *
     * @param routes routes
     * @param <T>    T
     * @return ArrayList<VueRouter<T>>
     */
    public static <T> ArrayList<VueRouter<T>> buildVueRouter(List<VueRouter<T>> routes) {
        if (routes == null) {
            return null;
        }
        List<VueRouter<T>> topRoutes = new ArrayList<>();
        VueRouter<T> router = new VueRouter<>();
        router.setName("系统主页");
        router.setPath("/main/MainPage");
        router.setComponent("HomePageView");
        router.setIcon("home");
        router.setChildren(null);
        router.setHasChildren(false);
        router.setMeta(new RouterMeta(false, true));
        topRoutes.add(router);

        routes.forEach(route -> {
            String parentId = route.getParentId();
            if (parentId == null || TOP_NODE_ID.equals(parentId)) {
                topRoutes.add(route);
                return;
            }
            for (VueRouter<T> parent : routes) {
                String id = parent.getId();
                if (id != null && id.equals(parentId)) {
                    if (parent.getChildren() == null) {
                        parent.initChildren();
                    }
                    parent.getChildren().add(route);
                    parent.setHasChildren(true);
                    route.setHasParent(true);
                    parent.setHasParent(true);
                    return;
                }
            }
        });
        router = new VueRouter<>();
        router.setPath("/profile");
        router.setName("个人中心");
        router.setComponent("personal/Profile");
        router.setIcon("none");
        router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);

        ArrayList<VueRouter<T>> list = new ArrayList<>();
        VueRouter<T> root = new VueRouter<>();
        root.setName("主页");
        root.setComponent("MenuView");
        root.setIcon("none");
        root.setPath("/");
        root.setRedirect("/main/MainPage");
        root.setChildren(topRoutes);
        list.add(root);

        root = new VueRouter<>();
        root.setName("404");
        root.setComponent("error/404");
        root.setPath("*");
        list.add(root);

        return list;
    }
}
