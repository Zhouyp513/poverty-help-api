package cn.poverty.repository.repository;

import cn.poverty.common.config.BaseRepository;
import cn.poverty.common.config.RedisEntityCacheHandler;
import cn.poverty.repository.entity.AuthMenu;
import cn.poverty.repository.result.AuthMenuQueryResult;
import cn.poverty.repository.result.AuthMenuResult;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 菜单Repository
 * @date 2019-08-22
 */
@Mapper
@CacheNamespace(implementation = RedisEntityCacheHandler.class,flushInterval = 60000)
public interface AuthMenuRepository extends BaseRepository<AuthMenu> {



    /**
     * 根据用户名查询用户能访问的菜单
     * @title: AuthMenuRepository.java
     * @author
     * @date 2019/4/28 11:05
     * @param userName 用户名
     * @param deleteStatus 是否删除 1 删除 2 未删除
     * @return List
     */
    @Select("<script>" +
            " SELECT " +
            " m.id AS id, " +
            " m.auth_menu_id AS authMenuId, " +
            " m.parent_id AS parentId, " +
            " m.path AS path, " +
            " m.component AS component, " +
            " m.menu_name AS menuName, " +
            " m.menu_icon AS menuIcon, " +
            " m.perms AS perms  " +
            "FROM " +
            " auth_menu m  " +
            "WHERE " +
            " m.menu_type != 1  " +
            " AND m.auth_menu_id IN ( " +
            " SELECT DISTINCT " +
            "  rm.menu_id  " +
            " FROM " +
            "  auth_role_menu rm " +
            "  LEFT JOIN auth_role r ON ( rm.auth_role_id = r.auth_role_id ) " +
            "  LEFT JOIN auth_user_role ur ON ( ur.auth_role_id = r.auth_role_id ) " +
            "  LEFT JOIN auth_user u ON ( u.auth_user_id = ur.auth_user_id )  " +
            " WHERE 1=1 and " +
            "  u.user_name = #{userName}  " +
            " and ur.delete_status = #{deleteStatus}" +
            " and r.delete_status = #{deleteStatus}" +
            " and u.delete_status = #{deleteStatus}" +
            " and rm.delete_status = #{deleteStatus}" +
            " and m.delete_status = #{deleteStatus}" +
            " )  " +
            "ORDER BY " +
            " m.order_num" +
            "</script>")
    List<AuthMenuQueryResult> queryAuthMenuByUserName(@Param("deleteStatus") Integer deleteStatus, @Param("userName") String userName);





    /**
     * 根据菜单ID查询具备此菜单的用户
     * @author
     * @date 2019-09-05
     * @param deleteStatus 是否删除状态
     * @param authMenuId 菜单ID
     * @return
     */
    @Select("<script>" +
            " SELECT " +
            " aur.auth_user_id as authUserId " +
            "FROM " +
            " auth_user_role  aur " +
            "WHERE " +
            " auth_role_id IN ( " +
            " SELECT " +
            "  rm.auth_role_id  " +
            " FROM " +
            "  auth_role_menu rm  " +
            " WHERE 1=1  and " +
            " rm.menu_id = #{authMenuId} and aur.delete_status = #{deleteStatus} " +
            " and rm.delete_status = #{deleteStatus} )" +
            "</script>")
    List<String> queryAuthUserIdByAuthMenuId(@Param("deleteStatus") Integer deleteStatus,@Param("authMenuId") String authMenuId);


    /**
     * 根据查询所有菜单的简略信息
     * @author
     * @date 2019-09-05
     * @param deleteStatus 是否删除状态
     * @return
     */
    @Select("<script>" +
            " SELECT " +
            " menu.auth_menu_id as authMenuId ,  menu.menu_name as menuName , menu.parent_id as parentId " +
            " from auth_menu menu WHERE 1=1  " +
            " and menu.delete_status = #{deleteStatus} " +
            "</script>")
    @Results(
            {
                    @Result(column = "authMenuId", property = "authMenuId", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "menuName", property = "menuName", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "parentId", property = "parentId", jdbcType = JdbcType.VARCHAR)

            }
    )
    List<AuthMenuResult> queryAllMenu(@Param("deleteStatus") Integer deleteStatus);

    /**
     * 根据菜单主键ID更新数据字典删除状态
     * @author
     * @date 2019-08-29
     * @param deleteStatus 是否删除的状态
     * @param authMenuIdList 菜单主键ID集合
     * @return
     */
    @Update("<script>"+
            "UPDATE auth_menu SET delete_status = #{deleteStatus} WHERE 1=1 and auth_menu_id in " +
            " <foreach item='item' index='index' collection='authMenuIdList' open='(' separator=',' close=')'>   " +
            "     #{item}  " +
            " </foreach> " +
            "</script>"
    )
    void updateDeleteStatusByAuthMenuIdListIn(@Param("deleteStatus") Integer deleteStatus,@Param("authMenuIdList") List<String> authMenuIdList);


}
