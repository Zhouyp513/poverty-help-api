package cn.poverty.interaction.resp.page;

/**
 * 可分页接口
 
 * 创建时间：2018/5/9 下午9:59
 */
public interface Paginable {

    /**
     * 总记录数
     *
     * @return
     */
    long getTotal();

    /**
     * 总页数
     *
     * @return
     */
    int getTotalPage();

    /**
     * 每页记录数
     *
     * @return
     */
    int getItemsPerPage();

    /**
     * 当前页号
     *
     * @return
     */
    int getCurrentPage();

}
