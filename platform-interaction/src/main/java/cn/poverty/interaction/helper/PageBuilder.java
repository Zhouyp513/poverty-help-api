package cn.poverty.interaction.helper;

import cn.poverty.interaction.resp.page.Pagination;
import com.github.pagehelper.Page;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * 构建分页结果

 * @time 2018/10/12
 * @description
 */
public class PageBuilder<T extends Serializable> implements Serializable{



    /**
     * 计算SQL limit 开始语句开始行号
     * @author
     * @date 2019-11-28
     * @param pageNum 页码数
     * @param pageSize 每页数量
     * @return int
     */
    public static int calStartRow(int pageNum,int pageSize){
        return pageNum > 0 ? (pageNum - 1) * pageSize : 0;
    }

    /**
     * 计算SQL limit 开始语句结束行号
     * @author
     * @date 2019-11-28
     * @param pageNum 页码数
     * @param pageSize 每页数量
     * @return int
     */
    public static int calEndRow(int pageNum,int pageSize){
        return calStartRow(pageNum,pageSize) + pageSize * (pageNum > 0 ? 1 : 0);
    }


    /**
     * 构建分页结果
     * @param page
     * @param list
     * @return
     */
    public static<T> Pagination buildPageResult(Page<Object> page, List<T> list){
        Pagination pagination = new Pagination();
        pagination.setData(list);
        Integer total = Integer.valueOf(String.valueOf(page.getTotal())).compareTo(BigInteger.ZERO.intValue()) > 0 ? Integer.valueOf(String.valueOf(page.getTotal())) : 0;
        pagination.setTotal(total);
        pagination.setItemsPerPage(page.getPageSize());
        pagination.setCurrentPage(page.getPageNum());
        pagination.setTotalPage(page.getPages());
        return pagination;
    }


}
