package cn.poverty.service;




import cn.poverty.interaction.req.archive.PovertyArchiveAddReq;
import cn.poverty.interaction.req.archive.PovertyArchivePageReq;
import cn.poverty.interaction.req.archive.PovertyArchiveUpdateReq;
import cn.poverty.interaction.resp.archive.PovertyArchiveResp;
import cn.poverty.interaction.resp.page.Pagination;

import java.util.List;

/**

 * @packageName cn.poverty.service
 * @Description: 救助金相关服务方法
 * @date 2021-10-20
 */
public interface PovertyArchiveService {


    /**
     * 新增
     *
     * @date 2021/2/15
     * @param addReq 新增Req
     */
    void addItem(PovertyArchiveAddReq addReq);

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
    Pagination<PovertyArchiveResp> queryByPage(
            PovertyArchivePageReq pageReq);

    /**
     * 查询所有项目
     *
     * @date 2021/10/25
     * @return java.util.List
     */
    List<PovertyArchiveResp> queryAllItem();

    /**
     * 更新
     *
     * @date 2021/4/2
     * @param updateReq 更新请求参数
     */
    void updateItem(PovertyArchiveUpdateReq updateReq);



}
