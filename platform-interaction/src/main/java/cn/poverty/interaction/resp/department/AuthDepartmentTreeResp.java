package cn.poverty.interaction.resp.department;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 部门树型结构查询Resp
 * @date 2019-08-23
 */
@Data
public class AuthDepartmentTreeResp implements Serializable {

    private static final long serialVersionUID = 4654347070858834982L;



    /**
     * 部门主键ID
     */
    private String departmentId;


    /**
     * 部门名称
     */
    private String departmentName;


    /**
     * 上级部门
     */
    private String parentId;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 创建时间
     */
    //@ApiModelProperty(value = "修改时间",notes ="修改时间" )
    @JSONField(format="yyyy-MM-dd HH:mm:ss",name = "updateTime")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    /**
     * 修改时间
     */
    //@ApiModelProperty(value = "修改时间",notes ="修改时间" )
    @JSONField(format="yyyy-MM-dd HH:mm:ss",name = "updateTime")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
