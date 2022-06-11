package cn.poverty.repository.repository;

import cn.poverty.common.config.BaseRepository;
import cn.poverty.repository.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.poverty.repository.repository
 * @Description: 附件->Repositpry
 * @date 2022-01-07
 */
@Mapper
public interface AttachmentRepository extends BaseRepository<Attachment> {

    /**
     * 根据业务主键批量删除
     * @title: AuthRoleRepository.java
     * @author
     * @date 2019/4/28 11:05
     * @param mainIdList 业务主键ID集合
     * @param deleteStatus 是否删除 1 删除 2 未删除
     * @return List
     */
    @Update("<script>" +
            " UPDATE attachment item set item.delete_status = #{deleteStatus} " +
            " WHERE item.attachment_id IN " +
            " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' > " +
            "          #{item} " +
            " </foreach>" +
            "</script>")
    void batchUpdateDeleteStatus(@Param("deleteStatus") Integer deleteStatus,
                                 @Param("mainIdList") List<String> mainIdList);
}
