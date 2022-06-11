package cn.poverty.repository.entity;

import cn.poverty.common.entity.BaseEntity;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;

/**
 * @author
 * @packageName cn.poverty.repository.entity
 * @Description: 用户角色关联实体
 * @date 2019-08-12
 */
@Entity
@Data
@Table(name="auth_user_role")
public class AuthUserRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 9099446914356030943L;


    /**
     * 用户角色关联主键ID
     */
    @Column(name = "auth_user_role_id",nullable = false)
    private String authUserRoleId = SnowflakeIdWorker.uniqueSequenceStr();


    /**
     * 用户ID
     */
    @Column(name = "auth_user_id",nullable = false)
    private String authUserId;


    /**
     * 角色ID
     */
    @Column(name = "auth_role_id",nullable = false)
    private String authRoleId;

}
