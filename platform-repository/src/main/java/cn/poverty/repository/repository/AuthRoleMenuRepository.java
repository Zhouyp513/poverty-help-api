package cn.poverty.repository.repository;

import cn.poverty.common.config.BaseRepository;
import cn.poverty.common.config.RedisEntityCacheHandler;
import cn.poverty.repository.entity.AuthRoleMenu;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 
 * @packageName cn.poverty.repository.repository
 * @Description: 角色->菜单Repository
 * @date 2019-08-12
 */
@Mapper
@CacheNamespace(implementation = RedisEntityCacheHandler.class,flushInterval = 60000)
public interface AuthRoleMenuRepository extends BaseRepository<AuthRoleMenu> {


    /**
      * 查询角色对应的权限菜单
      
      * @date 2019/9/4
      * @param authRoleId 角色ID
      * @return List
      */
    @Select(" select * from auth_role_menu where 1=1 and " +
            " auth_role_id = #{authRoleId} and delete_status = 2 "
    )
    List<AuthRoleMenu> queryRoleMenusByRoleId(@Param("authRoleId") String authRoleId);

    /**
      * 删除指定权限
      
      * @date 2019/9/4
      * @param authRoleId 角色ID
      * @param menuId 菜单ID
      */
    @Delete("DELETE FROM auth_role_menu WHERE auth_role_id = #{authRoleId} AND menu_id = #{menuId}")
    void deleteByRoleAndMenuId(@Param("authRoleId") String authRoleId,
                               @Param("menuId") String menuId);

    /**
     * 通过菜单ID删除指定权限
     
     * @date 2019/9/4
     * @param authMenuIdList 菜单ID集合
     */
    @Delete("<script>" +
            "  DELETE FROM auth_role_menu WHERE  1=1 AND menu_id IN " +
            "  <foreach item='item' index='index' collection='authMenuIdList' open='(' separator=',' close=')'>   " +
            "      #{item}  " +
            "  </foreach> " +
            "</script>")
    void deleteByMenuIdList(@Param("authMenuIdList") List<String> authMenuIdList);


    /**
     * 删除指定权限
     
     * @date 2019/9/4
     * @param authRoleIdList 角色ID集合
     */
    @Delete("<script>" +
            "  DELETE FROM auth_role_menu WHERE  1=1 AND auth_role_id IN " +
            "  <foreach item='item' index='index' collection='authRoleIdList' open='(' separator=',' close=')'>   " +
            "      #{item}  " +
            "  </foreach> " +
            "</script>")
    void deleteByAuthRoleIdList(@Param("authRoleIdList") List<String> authRoleIdList);
}
