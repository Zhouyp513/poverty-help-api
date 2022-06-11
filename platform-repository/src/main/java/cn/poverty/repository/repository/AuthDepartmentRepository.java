package cn.poverty.repository.repository;

import cn.poverty.common.config.BaseRepository;
import cn.poverty.common.config.RedisEntityCacheHandler;
import cn.poverty.repository.entity.AuthDepartment;
import cn.poverty.repository.result.AuthDepartmentResult;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 
 * @packageName cn.poverty.repository.repository
 * @Description: 部门Repository
 * @date 2019-08-12
 */
@Mapper
@CacheNamespace(implementation = RedisEntityCacheHandler.class,flushInterval = 60000)
public interface AuthDepartmentRepository extends BaseRepository<AuthDepartment> {



    /**
     * 根据查询所有部门的简略信息
     * 
     * @date 2019-09-05
     * @param deleteStatus 是否删除状态
     * @return
     */
    @Select("<script>" +
            " SELECT " +
            " department.department_id AS departmentId ,department.department_name AS departmentName ," +
            " department.order_num as orderNum , department.parent_id as parentId " +
            " from auth_department department WHERE 1=1  " +
            " and department.delete_status = #{deleteStatus} " +
            "</script>")
    @Results(
            {
                    @Result(column = "departmentId", property = "departmentId", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "departmentName", property = "departmentName", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "parentId", property = "parentId", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "orderNum", property = "orderNum", jdbcType = JdbcType.VARCHAR)

            }
    )
    List<AuthDepartmentResult> queryAllDepartment(@Param("deleteStatus") Integer deleteStatus);


    /**
     * 根据部门主键ID更新部门删除状态
     * 
     * @date 2019-08-29
     * @param deleteStatus 是否删除的状态
     * @param departmentIdList 部门主键ID集合
     * @return
     */
    @Update("<script>"+
            "UPDATE auth_department department SET department.delete_status = #{deleteStatus} WHERE 1=1 and department_id in " +
            " <foreach item='item' index='index' collection='departmentIdList' open='(' separator=',' close=')'>   " +
            "     #{item}  " +
            " </foreach> " +
            "</script>"
    )
    void updateDeleteStatusByDepartmentIdList(@Param("deleteStatus") Integer deleteStatus,
                                  @Param("departmentIdList") List<String> departmentIdList);

    /**
     * 删除指定部门
     
     * @date 2019/9/4
     * @param departmentIdList 菜单ID集合
     */
    @Delete("<script>" +
            "  DELETE FROM auth_department WHERE  1=1 AND department_id IN " +
            "  <foreach item='item' index='index' collection='departmentIdList' open='(' separator=',' close=')'>   " +
            "      #{item}  " +
            "  </foreach> " +
            "</script>")
    void deleteByDepartmentIdList(@Param("departmentIdList") List<String> departmentIdList);

}
