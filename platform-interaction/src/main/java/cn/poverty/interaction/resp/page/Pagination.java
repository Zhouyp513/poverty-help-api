package cn.poverty.interaction.resp.page;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.*;
import java.util.List;

/**
 * 分页封装对象
 
 * @time 2018/10/12
 * @description
 */
@Data
//@ApiModel
public class Pagination<T extends Serializable> extends BasePage implements
        Serializable, Paginable {


    private static final long serialVersionUID = 2634909391200457086L;


    /**
     * 当前页的数据
     */
    //@ApiModelProperty(value = "分页的数据，为一个集合",required = true,notes = "分页的数据，为一个集合",example = "list")
    @JSONField(name = "rows")
    private List<T> data;

    public Pagination() {
    }

    public Pagination(int pageNo, int pageSize, int totalCount) {
        super(pageNo, pageSize, totalCount);
    }

    public Pagination(int pageNo, int pageSize, int totalCount, List<T> data) {
        super(pageNo, pageSize, totalCount);
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
