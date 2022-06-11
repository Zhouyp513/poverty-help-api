package cn.poverty.repository.repository;
import cn.poverty.common.config.BaseRepository;
import cn.poverty.repository.entity.PovertyArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 贫困人员档案->Repository
 * @date 2019-08-29
 */
@Mapper
public interface PovertyArchiveRepository extends BaseRepository<PovertyArchive>{


    /**
     * 根据业务主键批量删除

     * @date 2019/4/28 11:05
     * @param mainIdList 业务主键ID集合
     * @param deleteStatus 是否删除 1 删除 2 未删除
     * @return List
     */
    @Update("<script>" +
            " UPDATE poverty_archive item set item.delete_status = #{deleteStatus} " +
            " WHERE item.poverty_archive_id IN " +
            " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' > " +
            "          #{item} " +
            " </foreach>" +
            "</script>")
    void batchUpdateDeleteStatus(@Param("deleteStatus") Integer deleteStatus,
                                 @Param("mainIdList") List<String> mainIdList);


}
