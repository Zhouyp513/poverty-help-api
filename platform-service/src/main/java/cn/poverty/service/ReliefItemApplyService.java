package cn.poverty.service;



import cn.poverty.interaction.req.item.apply.ReliefItemApplyAddReq;
import cn.poverty.interaction.req.item.apply.ReliefItemApplyPageReq;
import cn.poverty.interaction.req.item.apply.ReliefItemApplyReq;
import cn.poverty.interaction.req.item.apply.ReliefItemApplyUpdateReq;
import cn.poverty.interaction.resp.item.apply.ReliefItemApplyResp;
import cn.poverty.interaction.resp.page.Pagination;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author
 * @packageName cn.poverty.service
 * @Description: 救助金相关服务方法
 * @date 2021-10-20
 */
public interface ReliefItemApplyService {

    /**
     * 申请扶贫项目
     * @author
     * @date 2021/2/15
     * @param addReq 申请扶贫项目请求参数
     */
    void applyItem(ReliefItemApplyReq addReq);

    /**
     * 新增
     * @author
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    void addItem(ReliefItemApplyAddReq addReq);

    /**
     * 主键ID集合批量
     * @author
     * @date 2021/2/2
     * @param mainIdList 主键ID集合
     */
    void batchDeleteItem(List<String> mainIdList);


    /**
     * 分页查询
     * @author
     * @date 2021/2/15
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    Pagination<ReliefItemApplyResp> queryByPage(
            ReliefItemApplyPageReq pageReq);

    /**
     * 更新
     * @author
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    void updateItem(ReliefItemApplyUpdateReq updateReq);



}
