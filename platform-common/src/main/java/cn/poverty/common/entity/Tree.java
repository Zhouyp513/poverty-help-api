package cn.poverty.common.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统树工具实体
 * @date 2019-08-22
 */
@Data
public class Tree<T> {

    private String id;

    private String icon;

    /*title value key是最关键的要素*/
    private String title;

    private String value;

    private String key;

    private String text;

    private String permission;

    private String type;

    private Integer order;

    private String path;

    private String component;

    private List<Tree<T>> children;

    private String parentId;

    private boolean hasParent = false;

    private boolean hasChildren = false;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public void initChildren(){
        this.children = new ArrayList<>();
    }
}
