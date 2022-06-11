package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;
import java.time.Instant;
import java.util.Date;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统登陆日志实体
 * @date 2019-08-20
 */
@Entity
@Data
@Table(name="system_login_log")
public class SystemLoginLog extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 4871041033499766328L;


    /**
     * 系统登陆日志主键ID
     */
    @Column(name = "system_login_log_id",nullable = false)
    private String systemLoginLogId = SnowflakeIdWorker.uniqueSequenceStr();


    /**
     * 用户名称
     */
    @Column(name = "user_name",unique = true)
    private String userName;

    /**
     * 登录时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column(name = "login_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date loginTime = Date.from(Instant.now());

    /**
     * 登录地点
     */
    @Column(name = "location",unique = true)
    private String location;


    /**
     * 登陆IP
     */
    @Column(name = "login_ip",unique = true)
    private String ip;

}
