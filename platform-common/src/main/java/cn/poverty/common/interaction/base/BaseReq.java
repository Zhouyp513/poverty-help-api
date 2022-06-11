package cn.poverty.common.interaction.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 基础的BaseReq 所有的请求参数对象应该继承这个
 
 * @time 2018/10/8
 * @description
 */
@Data
//@ApiModel
public class BaseReq implements Serializable{


    private static final long serialVersionUID = 3601812827915344334L;

    /**
     * 接口请求流水号
     */
    //@ApiModelProperty(value = "接口请求流水号",required = true,notes = "接口请求流水号",example = "1")
    private String sequenceNo;

    /**
     * 请求时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    //@ApiModelProperty(value = "创建时间",notes ="创建时间" )
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reqTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());

    /**
     * 业务发起方系统名称
     */
    //@ApiModelProperty(value = "业务发起方系统名称",required = true,notes = "业务发起方系统名称",example = "1")
    private String serviceCode;
}
