package cn.poverty.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: VUE前端路由Resp
 * @date 2019-08-22
 */
@Data
public class VueRouter<T> implements Serializable {

    @JSONField(serialize = false)
    private String id;

    @JSONField(serialize = false)
    private String parentId;

    private String path;

    private String name;

    private String component;

    private String icon;


    private String redirect;

    @JSONField(serialize = false)
    private String perms;

    private RouterMeta meta;

    private List<VueRouter<T>> children;

    @JSONField(serialize = false)
    private boolean hasParent;

    @JSONField(name = "hasChildrenRouter")
    private boolean hasChildren;

    public void initChildren(){
        this.children = new ArrayList<>();
    }


}
