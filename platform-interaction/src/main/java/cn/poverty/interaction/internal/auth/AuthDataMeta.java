package cn.poverty.interaction.internal.auth;

import lombok.Data;

import java.io.*;

/**
 * @author 
 * @packageName cn.common.interaction.internal.auth
 * @Description: 数据隔离基元信息
 * @date 2021-05-10
 */
@Data
public class AuthDataMeta implements Serializable {

    private static final long serialVersionUID = 4517668954035234012L;

    /**
     * 地区ID
     */
    private String  regionId;

    /**
     * 企业ID
     */
    private String  corpBaseDataId;

}
