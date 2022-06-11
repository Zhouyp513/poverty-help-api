package cn.poverty.service;

import cn.poverty.interaction.internal.auth.AsyncAuthMeta;
import cn.poverty.interaction.req.user.AuthUserImportReq;

import java.util.List;

/**

 * @packageName cn.poverty.service
 * @Description: 异步处理服务
 * @date 2021-09-30
 */
public interface AsyncHandlerService {

    /**
     * 导入系统用户
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2022/1/7
     * @param importList 导入数据
     */
    void importUser(List<AuthUserImportReq> importList);


    /**
     * 异步处理登录
     *
     * @date 2021/9/30
     * @param authMeta 异步认证所需的基元信息
     */
    void handleLoginAsync(AsyncAuthMeta authMeta);


    /**
     * 当前用户登出
     *
     * @date 2019-08-21
     * @param authUserId 当前用户的ID
     */
    void asyncHandleLogOut(String authUserId);

}
