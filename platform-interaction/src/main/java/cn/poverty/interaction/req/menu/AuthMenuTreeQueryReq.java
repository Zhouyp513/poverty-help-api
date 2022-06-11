package cn.poverty.interaction.req.menu;

import cn.poverty.common.interaction.base.page.BasePageReq;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统认证菜单分页查询Req
 * @date 2019-08-26
 */
@Data
public class AuthMenuTreeQueryReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = -319005213496158250L;


    /**
      * 菜单名称
      */
    private String menuName;



    /**
     * 菜单类型 0->按钮,1->菜单
     */
    @JSONField(name = "type")
    private String menuType;


    /**
     * 是否需要查询所有
     */
    private Boolean needAll = false;
}
