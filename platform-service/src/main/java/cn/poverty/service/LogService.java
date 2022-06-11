package cn.poverty.service;

import cn.poverty.interaction.req.log.SystemApiLogPageReq;
import cn.poverty.interaction.resp.log.SystemApiLogPageQueryResp;
import cn.poverty.interaction.resp.page.Pagination;

/**

 * @projectName poverty-help-api
 * @Description: 系统日志Service
 * @date 2020-01-10
 */
public interface LogService {


    /**
     * 分页查询系统日志
     *
     * @date 2020-01-10
     * @param systemApiLogPageReq 系统API日志查询请求Req
     * @return Pagination
     */
    Pagination<SystemApiLogPageQueryResp> querySystemApiLogByPage(SystemApiLogPageReq systemApiLogPageReq);

}
