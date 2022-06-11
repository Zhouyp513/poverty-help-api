package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.poverty.repository.entity
 * @Description: 附件实体
 * @date 2022-01-07
 */
@Entity
@Data
@Table(name="attachment")
public class Attachment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 631696376760973963L;

    /**
     * 关联ID
     */
    @Column(name = "attachment_id")
    private String attachmentId = SnowflakeIdWorker.uniqueSequenceStr();

    /**
     * 关联ID
     */
    @Column(name = "other_id")
    private String otherId;


    /**
     * 附件访问URL
     */
    @Column(name = "attach_url")
    private String attachUrl;

    /**
     * 附件业务类型 1:扶贫资金 2:扶贫项目
     */
    @Column(name = "attach_type")
    private Integer attachType;


}
