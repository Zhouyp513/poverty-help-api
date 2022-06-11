package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;

/**
 * 系统API请求log
 * @title: Job.java
 * @author
 * @date 2019/4/24 11:13
 */
@Entity
@Data
@Table(name = "system_api_log")
@NoArgsConstructor
public class SystemApiLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6546838388765409121L;

    /**
     * 系统API请求LOG主键ID
     */
    @Column(name = "system_api_log_id")
    private String systemApiLogId = SnowflakeIdWorker.uniqueSequenceStr();

    /**
     * 操作用户
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 操作
     */
    @Column(name = "operation")
    private String operation;


    /**
     * 操作位置
     */
    @Column(name = "location")
    private String location;


    /**
     * 方法名
     */
    @Column(name = "method")
    private String method;

    /**
     * 参数
     */
    @Column(columnDefinition = "text")
    private String params;


    /**
     * 请求ip
     */
    @Column(name = "request_ip")
    private String requestIp;

    /**
     * 请求耗时
     */
    @Column(name = "time")
    private Long time;


}
