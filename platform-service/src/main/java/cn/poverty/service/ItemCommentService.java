package cn.poverty.service;




import cn.poverty.interaction.req.item.comment.ItemCommentAddReq;
import cn.poverty.interaction.req.item.comment.ItemCommentPageReq;
import cn.poverty.interaction.req.item.comment.ItemCommentUpdateReq;
import cn.poverty.interaction.resp.item.comment.ItemCommentResp;
import cn.poverty.interaction.resp.page.Pagination;

import java.util.List;

/**
 
 * @packageName cn.poverty.service
 * @Description: 扶贫项目相关服务方法
 * @date 2021-10-20
 */
public interface ItemCommentService {

    /**
     * 新增
     * 
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    void addItem(ItemCommentAddReq addReq);

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
    Pagination<ItemCommentResp> queryByPage(
            ItemCommentPageReq pageReq);

    /**
     * 更新
     * 
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    void updateItem(ItemCommentUpdateReq updateReq);



}
