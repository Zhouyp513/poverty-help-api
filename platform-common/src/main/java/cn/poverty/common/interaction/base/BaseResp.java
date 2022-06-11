package cn.poverty.common.interaction.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.*;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 
 * @projectName poverty-help-api
 * @Description:
 * @date 2020-02-20
 */
@Data
public class BaseResp implements Serializable {


    private static final long serialVersionUID = -4697059527481147141L;


    /**
     * 主键
     */
    private Long id;


    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    //@ApiModelProperty(value = "创建时间",notes ="创建时间" )
    private LocalDateTime createTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());


    /**
     * 修改时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());

    /**
     * 操作人ID
     */
    private String operatorId = "0";

    /**
     * 是否删除 1删除 2未删除
     */
    //@ApiModelProperty(value = "是否删除 1删除 2未删除",notes ="是否删除 1删除 2未删除" )
    private Integer deleteStatus;
}
