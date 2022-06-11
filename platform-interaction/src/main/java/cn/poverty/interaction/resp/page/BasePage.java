package cn.poverty.interaction.resp.page;

import lombok.Data;

/**
 * 基础分页对象封装
 
 * @time 2018/10/12
 * @description
 */
@Data
//@ApiModel
public class BasePage implements Paginable {


    public static final int DEF_COUNT = 10;

    //@ApiModelProperty(value = "总条数",required = true,notes = "总条数",example = "1000")
    protected Integer total = 0;

    //@ApiModelProperty(value = "每页条数",required = true,notes = "每页条数",example = "10")
    protected Integer itemsPerPage = DEF_COUNT;

    //@ApiModelProperty(value = "当前页码数",required = true,notes = "当前页码数",example = "1")
    protected Integer currentPage = 1;

    //@ApiModelProperty(value = "总页数",required = true,notes = "总页数",example = "list")
    protected Integer totalPage = 1;

    public BasePage() {
    }

    public BasePage(int pageNo, int pageSize, int totalCount) {
        if (totalCount <= 0) {
            this.total = 0;
        } else {
            this.total = totalCount;
        }
        if (pageSize <= 0) {
            this.itemsPerPage = DEF_COUNT;
        } else {
            this.itemsPerPage = pageSize;
        }
        if (pageNo <= 0) {
            this.currentPage = 1;
        } else {
            this.currentPage = pageNo;
        }
        if ((this.currentPage - 1) * this.itemsPerPage >= totalCount) {
            if (this.total / this.itemsPerPage == 0) {
                this.currentPage = 1;
            } else {
                this.currentPage = (int)this.total / this.itemsPerPage;
            }
        }
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public int getItemsPerPage() {
        return itemsPerPage;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public int getTotalPage() {
        int totalPage = (int) total / itemsPerPage;
        if (total % itemsPerPage != 0 || totalPage == 0) {
            totalPage++;
        }
        return totalPage;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setItemsPerPage(int pageSize) {
        this.itemsPerPage = pageSize;
    }

    public void setCurrentPage(int pageNo) {
        this.currentPage = pageNo;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
