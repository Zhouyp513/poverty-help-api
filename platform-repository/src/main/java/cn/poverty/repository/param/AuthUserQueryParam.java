package cn.poverty.repository.param;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 * 用户查询参数封装
 * @projectName poverty-help-api
 * @title: poverty.repository.query-AuthUserQueryParam
 * @date 2019/4/28 10:32
 */
@Data
public class AuthUserQueryParam implements Serializable {

    private static final long serialVersionUID = 1536126091523772273L;


    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 更新时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 用户名
     */
    private String  userName;



    /**
     * 部门ID
     */
    private String departmentId;

}
