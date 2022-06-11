package cn.poverty.common.interaction;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**

 * @projectName poverty-help-api
 * @Description: 通过IP查询出地址的Resp封装
 * @date 2019-08-21
 */
@Data
public class AddressFromIpVo implements Serializable {

    private static final long serialVersionUID = -2662844413923992707L;



    /**
      * 错误码
      */
    @JSONField(name="errno")
    private String errNo;

    /**
     * 错误信息
     */
    @JSONField(name="errmsg")
    private String errMessage;

    /**
     * 数据
     */
    @JSONField(name="data")
    private String data;


}
