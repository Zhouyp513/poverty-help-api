package cn.poverty.repository.result;

import cn.poverty.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 
 * @packageName cn.poverty.repository.entity
 * @Description:
 * @date 2019-08-12
 */
@Data
public class AuthUserRoleResult extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -4322094612076860322L;


    /**
     * 用户ID
     */
    private Long userId;


    /**
     * 角色ID
     */
    private Long authRoleId;

}
