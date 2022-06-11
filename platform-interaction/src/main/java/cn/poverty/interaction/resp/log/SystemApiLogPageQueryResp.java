package cn.poverty.interaction.resp.log;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统日志分页查询Resp
 * @date 2020-01-10
 */
@Data
public class SystemApiLogPageQueryResp implements Serializable {


    private static final long serialVersionUID = 8264868419283309838L;


    /**
     * 系统API请求LOG主键ID
     */
    private String systemApiLogId;

    /**
     * 操作用户
     */
    private String userName;

    /**
     * 操作
     */
    private String operation;


    /**
     * 操作位置
     */
    private String location;


    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    private String params;


    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 请求耗时
     */
    private Long time;


    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());
}
