package cn.poverty.interaction.req.log;

import cn.poverty.common.interaction.base.page.BasePageReq;
import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统日志请求Req
 * @date 2020-01-10
 */
@Data
public class SystemApiLogPageReq extends BasePageReq implements Serializable {


    private static final long serialVersionUID = -669500717860457212L;


    /**
     * 操作用户
     */
    private String userName;

    /**
     * 操作
     */
    private String operation;


}
