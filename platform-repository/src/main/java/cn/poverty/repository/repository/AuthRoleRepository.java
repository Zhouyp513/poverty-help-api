package cn.poverty.repository.repository;

import cn.poverty.common.config.BaseRepository;
import cn.poverty.common.config.RedisEntityCacheHandler;
import cn.poverty.repository.entity.AuthRole;
import cn.poverty.repository.result.AuthRoleResult;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * 角色Repository
 * @title: AuthPermissionRepository.java
 
 * @date 2019/4/24 11:21
 * @return
 */
@Mapper
@CacheNamespace(implementation = RedisEntityCacheHandler.class,flushInterval = 60000)
public interface AuthRoleRepository extends BaseRepository<AuthRole> {


    /**
     * 根据用户ID查询用户的角色关系
     * @title: AuthRoleRepository.java
     
     * @date 2019/4/28 11:05
     * @param userName 用户名
     * @param deleteStatus 是否删除 1 删除 2 未删除
     * @return List
     */
    @Select("<script>" +
            " SELECT " +
            " r.id AS id, " +
            " r.auth_role_id AS authRoleId, " +
            " r.role_name AS roleName, " +
            " r.role_code AS roleCode, " +
            " r.remark AS remark, " +
            " r.create_time AS createTime, " +
            " r.update_time AS updateTime, " +
            " r.delete_status AS deleteStatus, " +
            " r.operator_id AS operatorId, " +
            " u.auth_user_id AS authUserId  " +
            "FROM " +
            " auth_role r " +
            " LEFT JOIN auth_user_role ur ON ( r.auth_role_id = ur.auth_role_id ) " +
            " LEFT JOIN auth_user u ON ( u.auth_user_id = ur.auth_user_id )  " +
            "WHERE " +
            " 1 = 1 " +
            " AND u.user_name = #{userName} and u.delete_status = #{deleteStatus} " +
            " and ur.delete_status = #{deleteStatus}" +
            "</script>")
    List<AuthRoleResult> queryUserRoleDataByUserName(@Param("deleteStatus") Integer deleteStatus,
                                                     @Param("userName") String userName);


    /**
     * 根据用户ID查询用户的角色关系
     * @title: AuthRoleRepository.java
     
     * @date 2019/4/28 11:05
     * @param authUserIdList 用户业务主键ID集合
     * @param deleteStatus 是否删除 1 删除 2 未删除
     * @return List
     */
    @Select("<script>" +
            " SELECT " +
            " r.id AS id, " +
            " r.auth_role_id AS authRoleId, " +
            " r.role_name AS roleName, " +
            " r.role_code AS roleCode, " +
            " r.remark AS remark, " +
            " r.create_time AS createTime, " +
            " r.update_time AS updateTime, " +
            " r.delete_status AS deleteStatus, " +
            " r.operator_id AS operatorId, " +
            " u.auth_user_id AS authUserId  " +
            "FROM " +
            " auth_role r " +
            " LEFT JOIN auth_user_role ur ON ( r.auth_role_id = ur.auth_role_id ) " +
            " LEFT JOIN auth_user u ON ( u.auth_user_id = ur.auth_user_id )  " +
            "WHERE " +
            " 1 = 1 " +
            " AND u.auth_user_id IN  " +
            " <foreach collection='authUserIdList' item= 'item' index= 'index' open='(' separator=',' close=')' > " +
            "          #{item} " +
            " </foreach>" +
            " and ur.delete_status = #{deleteStatus}" +
            " and r.delete_status = #{deleteStatus}" +
            " and u.delete_status = #{deleteStatus}" +
            "</script>")
    List<AuthRoleResult> queryUserRoleByUserIdList(@Param("deleteStatus") Integer deleteStatus,
                                                   @Param("authUserIdList") List<String> authUserIdList);

    /**
      * 删除
      
      * @date 2019/9/4
      * @param authRoleId
      * @return
      */
    @Update({
            "<script>"
            + "UPDATE auth_role SET delete_status = 1 "
            + " WHERE auth_role_id = #{authRoleId} "
            +"</script>"
    })
    void deleteByRoleId(@Param("authRoleId") String authRoleId);

    /**
      * 根据角色名字查询
      
      * @date 2019/9/25
      * @param roleName
      * @return
      */
    @Select("<script>" +
            " SELECT * " +
            "            FROM auth_role " +
            "            WHERE role_name=#{roleName}" +
            "</script>")
    AuthRole queryByName(@Param("roleName") String roleName);

    /**
     * 根据角色代码查询角色ID
     
     * @date 2019/9/25
     * @param roleCode 角色代码
     * @param deleteStatus 是否删除 1 删除 2 未删除
     * @return
     */
    @Select("<script>" +
            " SELECT item.auth_role_id AS authRoleId " +
            " FROM auth_role item " +
            " WHERE 1=1 AND  item.role_code=#{roleCode}" +
            " AND item.delete_status = #{deleteStatus}" +
            "</script>")
    String queryAuthRoleIdByCode(@Param("deleteStatus") Integer deleteStatus,
                                   @Param("roleCode") String roleCode);


    /**
     * 根据角色代码查询角色ID
     
     * @date 2019/9/25
     * @param mainId 业务主键ID
     * @param deleteStatus 是否删除 1 删除 2 未删除
     * @return
     */
    @Select("<script>" +
            " SELECT item.role_code AS roleCode " +
            " FROM auth_role item " +
            " WHERE 1=1 AND  item.auth_user_id=#{mainId}" +
            " AND item.delete_status = #{deleteStatus}" +
            "</script>")
    String queryRoleCodeByMainId(@Param("deleteStatus") Integer deleteStatus,
                                 @Param("mainId") String mainId);

}
