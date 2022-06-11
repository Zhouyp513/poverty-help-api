package cn.poverty.service;



import cn.poverty.interaction.req.fund.apply.FundApplyReq;
import cn.poverty.interaction.req.fund.apply.ReliefFundApplyAddReq;
import cn.poverty.interaction.req.fund.apply.ReliefFundApplyPageReq;
import cn.poverty.interaction.req.fund.apply.ReliefFundApplyUpdateReq;
import cn.poverty.interaction.resp.fund.apply.ReliefFundApplyResp;
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
public interface ReliefFundApplyService {

    /**
     * 申请救助金
     * @author
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    void applyFund(FundApplyReq addReq);

    /**
     * 新增
     * @author
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    void addItem(ReliefFundApplyAddReq addReq);

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
    Pagination<ReliefFundApplyResp> queryByPage(
            ReliefFundApplyPageReq pageReq);

    /**
     * 更新
     * @author
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    void updateItem(ReliefFundApplyUpdateReq updateReq);



}
