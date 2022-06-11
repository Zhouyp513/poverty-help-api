package cn.poverty.repository.repository;

import cn.poverty.common.config.BaseRepository;
import cn.poverty.repository.entity.AuthUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description:
 * @date 2019-08-13
 */
@Mapper
public interface AuthUserRoleRepository extends BaseRepository<AuthUserRole> {


    /**
     * 根据角色Code查询角色对应的所有authUserId
     * @author
     * @date 2019/9/4
     * @param roleCode 用户ID集合
     */
    @Select("<script>" +
            "  SELECT  " +
            " authUserRole.auth_user_id AS  authUserId " +
            " FROM  " +
            "  auth_role authRole  " +
            "  RIGHT JOIN auth_user_role authUserRole ON authRole.auth_role_id = authUserRole.auth_role_id   " +
            " WHERE  " +
            " 1 = 1   " +
            " AND authRole.role_code = #{roleCode}  " +
            "</script>")
    List<String> queryUserIdByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 删除用户的角色关联信息
     * @author
     * @date 2019/9/4
     * @param authUserIdList 用户ID集合
     */
    @Delete("<script>" +
            "  DELETE FROM auth_user_role WHERE 1=1 AND auth_user_id IN " +
            "  <foreach item='item' index='index' collection='authUserIdList' open='(' separator=',' close=')'>   " +
            "      #{item}  " +
            "  </foreach> " +
            "</script>")
    void deleteByAuthUserIdList(@Param("authUserIdList") List<String> authUserIdList);

}
