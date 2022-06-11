package cn.poverty.repository.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 * @projectName poverty-help-api
 * @Description:
 * @date 2019-08-13
 */
@Data
public class AuthRoleResult implements Serializable {

    private static final long serialVersionUID = -3023799429852802438L;


    /**
     * 主键
     */
    private Long id;

    /**
     * 系统用户主键ID
     */
    private String authUserId;

    /**
     * 系统角色主键ID
     */
    private String authRoleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色代码
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String remark;

    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 修改时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 是否删除
     */

    private Integer deleteStatus;



}
