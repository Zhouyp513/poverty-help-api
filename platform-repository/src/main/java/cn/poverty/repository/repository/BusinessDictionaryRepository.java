package cn.poverty.repository.repository;
import cn.poverty.common.config.BaseRepository;
import cn.poverty.common.config.RedisEntityCacheHandler;
import cn.poverty.repository.entity.BusinessDictionary;
import cn.poverty.repository.result.BusinessDictResult;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**

 * @packageName cn.poverty.repository.entity
 * @Description: 业务字典Repository
 * @date 2019-08-12
 */
@Mapper
@CacheNamespace(implementation = RedisEntityCacheHandler.class,flushInterval = 60000)
public interface BusinessDictionaryRepository extends BaseRepository<BusinessDictionary> {


    /**
     * 根据系统业务字典主键ID系统字典信息
     *
     * @date 2019-08-29
     * @param deleteStatus 是否删除的状态
     * @param businessDictionaryIdList 系统业务字典主键ID集合
     * @return
     */
    @Update("<script>"+
            "UPDATE business_dictionary SET delete_status = #{deleteStatus} WHERE 1=1 and business_dictionary_id in " +
            " <foreach item='item' index='index' collection='businessDictionaryIdList' open='(' separator=',' close=')'>   " +
            "     #{item}  " +
            " </foreach> " +
            "</script>"
    )
    void updateDeleteStatusByIdListIn(@Param("deleteStatus") Integer deleteStatus, @Param("businessDictionaryIdList") List<String> businessDictionaryIdList);

    /**
     * 根据业务主键ID查询字典的值
     *
     * @date 2019-08-13
     * @param deleteStatus 是否删除 1 删除 2 未删除
     * @param businessDictionaryId 业务主键ID
     * @return List
     */
    @Select( "<script>" +
            " select dict.dict_value as dictValue " +
            " from business_dictionary dict  " +
            " where 1=1 and business_dictionary_id = #{businessDictionaryId} " +
            " and dict.delete_status = #{deleteStatus} " +
            "</script>"
    )
    String queryValueByMainId(@Param("deleteStatus") Integer deleteStatus, @Param("businessDictionaryId") String businessDictionaryId);

    /**
     * 根据主键查询系统业务字典
     *
     * @date 2021/4/22
     * @param deleteStatus  是否删除 1删除 2未删除
     * @return
     */
    @Select( "<script>" +
            " select " +
            " item.business_dictionary_id AS businessDictionaryId, " +
            " item.dict_type AS dictType,item.dict_key AS dictKey," +
            " item.dict_value AS dictValue," +
            " item.sort_index AS sortIndex, " +
            " item.dict_remark AS dictRemark " +
            " FROM business_dictionary item WHERE 1=1 " +
            " and item.delete_status = #{deleteStatus} " +
            "</script>"
    )
    @Results(
            {
                    @Result(column = "businessDictionaryId", property = "businessDictionaryId", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictType", property = "dictType", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictKey", property = "dictKey", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictValue", property = "dictValue", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "sortIndex", property = "sortIndex", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictRemark", property = "dictRemark", jdbcType = JdbcType.VARCHAR),

            }
    )
    @Options()
    List<BusinessDictResult> queryAllDict(@Param("deleteStatus") Integer deleteStatus);

    /**
      * 根据主键查询系统业务字典
      *
      * @date 2021/4/22
      * @param businessDictionaryId 业务主键ID
      * @param deleteStatus  是否删除 1删除 2未删除
      * @return
      */
    @Select( "<script>" +
            " select " +
            " item.business_dictionary_id AS businessDictionaryId, " +
            " item.dict_type AS dictType," +
            " item.dict_key AS dictKey," +
            " item.dict_value AS dictValue," +
            " item.sort_index AS sortIndex, " +
            " item.dict_remark AS dictRemark " +
            " from business_dictionary item  " +
            " where 1=1 and item.business_dictionary_id = #{businessDictionaryId} " +
            " and item.delete_status = #{deleteStatus} " +
            "</script>"
    )
    @Results(
            {
                    @Result(column = "businessDictionaryId", property = "businessDictionaryId", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictType", property = "dictType", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictKey", property = "dictKey", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictValue", property = "dictValue", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "sortIndex", property = "sortIndex", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictRemark", property = "dictRemark", jdbcType = JdbcType.VARCHAR),

            }
    )
    @Options()
    BusinessDictResult queryByMainId(@Param("deleteStatus") Integer deleteStatus,
                                     @Param("businessDictionaryId") String businessDictionaryId);


    /**
     * 根据主键ID集合查询系统业务字典
     *
     * @date 2021/4/22
     * @param mainIdList 业务主键ID集合
     * @param deleteStatus  是否删除 1删除 2未删除
     * @return
     */
    @Select( "<script>" +
            " select " +
            " item.business_dictionary_id AS businessDictionaryId, " +
            " item.dict_type AS dictType,item.dict_key AS dictKey," +
            " item.dict_value AS dictValue," +
            " item.sort_index AS sortIndex, " +
            " item.dict_remark AS dictRemark " +
            " from business_dictionary item  " +
            " where 1=1 and item.business_dictionary_id IN " +
            " <foreach item='item' index='index' collection='mainIdList' open='(' separator=',' close=')'>   " +
            "     #{item}  " +
            " </foreach> " +
            " and item.delete_status = #{deleteStatus} " +
            "</script>"
    )
    @Results(
            {
                    @Result(column = "businessDictionaryId", property = "businessDictionaryId", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictType", property = "dictType", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictKey", property = "dictKey", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictValue", property = "dictValue", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "sortIndex", property = "sortIndex", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "dictRemark", property = "dictRemark", jdbcType = JdbcType.VARCHAR),

            }
    )
    @Options()
    List<BusinessDictResult> queryByMainIdListIn(@Param("deleteStatus") Integer deleteStatus,
                                     @Param("mainIdList") List<String> mainIdList);


    /**
     * 根据字典值查询系统业务字典业务主键ID
     *
     * @date 2021/4/22
     * @param dctValue 字典值
     * @param deleteStatus  是否删除 1删除 2未删除
     * @return
     */
    @Select( "<script>" +
            " select " +
            " item.business_dictionary_id AS businessDictionaryId " +
            " from business_dictionary item  " +
            " where 1=1 and item.dict_value LIKE CONCAT('%',#{dctValue},'%') " +
            " and item.delete_status = #{deleteStatus} " +
            "</script>"
    )
    @Options()
    List<String> queryMainIdValueLike(@Param("deleteStatus") Integer deleteStatus,
                                                 @Param("dctValue") String dctValue);


}
