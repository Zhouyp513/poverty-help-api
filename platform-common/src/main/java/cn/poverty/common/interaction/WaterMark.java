package cn.poverty.common.interaction;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.*;

/**
 * WaterMark
 
 * @packageName cn.poverty.common.interaction
 * @date 2021-01-26
 */
@Data
public class WaterMark  implements Serializable {

    /**
     * 时间戳做转换的时候，记得先乘以1000，再通过simpledateformat完成date类型转换
     */
    private Long timestamp;

    /**
     * appId
     */
    @JSONField(name = "appid")
    private String appId;
}
