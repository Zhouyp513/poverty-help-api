package cn.poverty.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * 此类是基础的实体类
 * MappedSuperclass 此标签会把父类的字段映射到子类上面
 
 * @time 2018/10/12
 * @description
 */
@MappedSuperclass
@Data
public class BaseEntity implements Serializable {


    private static final long serialVersionUID = -279378218553793536L;


    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    //@ApiModelProperty(value = "主键",notes ="主键" )
    private Long id;


    /**
     * 创建时间
     */
    //@ApiModelProperty(value = "创建时间",notes ="创建时间" )
    @Column(name = "create_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());


    /**
     * 修改时间
     */
    //@ApiModelProperty(value = "修改时间",notes ="修改时间" )
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private LocalDateTime updateTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());

    /**
     * 数据删除时间

    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    @Column(name = "delete_time")
    //@ApiModelProperty(value = "删除时间",notes ="删除时间" )
    //private Long deleteTime = 0L;
    private Date deleteTime = Date.from(Instant.now()); */

    /**
     * 操作人ID
     */
    @Column(name = "operator_id")
    private String operatorId = "0";

    /**
     * 是否删除 1删除 2未删除
     */
    //@ApiModelProperty(value = "是否删除 1删除 2未删除",notes ="是否删除 1删除 2未删除" )
    @Column(name = "delete_status")
    private Integer deleteStatus = Integer.valueOf(2);
}
