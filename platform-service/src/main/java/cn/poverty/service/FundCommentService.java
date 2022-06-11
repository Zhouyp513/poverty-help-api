package cn.poverty.service;




import cn.poverty.interaction.req.fund.comment.FundCommentAddReq;
import cn.poverty.interaction.req.fund.comment.FundCommentPageReq;
import cn.poverty.interaction.req.fund.comment.FundCommentUpdateReq;
import cn.poverty.interaction.resp.fund.comment.FundCommentResp;
import cn.poverty.interaction.resp.page.Pagination;

import java.util.List;

/**

 * @packageName cn.poverty.service
 * @Description: 救助金相关服务方法
 * @date 2021-10-20
 */
public interface FundCommentService {

    /**
     * 新增
     *
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    void addItem(FundCommentAddReq addReq);

    /**
     * 主键ID集合批量
     *
     * @date 2021/2/2
     * @param mainIdList 主键ID集合
     */
    void batchDeleteItem(List<String> mainIdList);


    /**
     * 分页查询
     *
     * @date 2021/2/15
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    Pagination<FundCommentResp> queryByPage(
            FundCommentPageReq pageReq);

    /**
     * 更新
     *
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    void updateItem(FundCommentUpdateReq updateReq);



}
