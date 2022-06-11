package cn.poverty.common.interaction.base.auth;

import cn.poverty.common.interaction.base.page.BasePageReq;
import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poverty.interaction.base.auth
 * @Description: 基本数据权限分页查询Req
 * @date 2021-05-09
 */
@Data
public class AuthDataPageReq extends BasePageReq implements Serializable {


    private static final long serialVersionUID = 3689769231060249079L;

    /**
     * 地区ID
     */
    private String  regionId;

    /**
     * 企业ID
     */
    private String  corpBaseDataId;


}
