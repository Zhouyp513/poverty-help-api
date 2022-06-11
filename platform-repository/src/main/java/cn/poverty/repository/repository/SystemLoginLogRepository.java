package cn.poverty.repository.repository;

import cn.poverty.common.config.BaseRepository;
import cn.poverty.repository.entity.SystemLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 系统登陆日志Repository
 * @date 2019-08-21
 */
@Mapper
public interface SystemLoginLogRepository extends BaseRepository<SystemLoginLog> {




    /**
     * 根据用户名称查询用户详情信息
     * @author
     * @date 2019-08-13
     * @param deleteStatus 删除状态 是否删除 1删除 2未删除
     * @return Long
     */
    @Select( "<script>" +
            " select count(1) from system_login_log where delete_status = #{deleteStatus} "
            +"</script>"
    )
    Integer queryTotalVisitCount(@Param("deleteStatus") Integer deleteStatus);





    /**
     * 统计今日访问量
     * @author
     * @date 2019-08-22
     * @param deleteStatus 删除状态 是否删除 1删除 2未删除
     * @return Integer
     */
    @Select( "<script>" +
            " select count(1) from system_login_log where datediff(login_time,now()) = 0 and delete_status = #{deleteStatus} "
            +"</script>"
    )
    Integer queryTodayVisitCount(@Param("deleteStatus") Integer deleteStatus);






    /**
     * 统计今日IP
     * @author
     * @date 2019-08-22
     * @param deleteStatus 删除状态 是否删除 1删除 2未删除
     * @return Integer
     */
    @Select( "<script>" +
            "  select count(distinct(login_ip)) from system_login_log where datediff(login_time,now())=0 and delete_status = #{deleteStatus} "
            +"</script>"
    )
    Integer queryTodayIp(@Param("deleteStatus") Integer deleteStatus);


    /**
     * 统计最近七天访问量
     * @author
     * @date 2019-08-22
     * @param deleteStatus 删除状态 是否删除 1删除 2未删除
     * @return Integer
     */
    @Select( "<script>" +
            "  select count(distinct(login_ip)) from system_login_log where datediff(login_time,now()) = 0 and delete_status = #{deleteStatus} "
            +"</script>"
    )
    Integer queryLastSevenDaysVisitCount(@Param("deleteStatus") Integer deleteStatus);



    /**
     * 根据登陆名和时间统计访问量
     * @author
     * @date 2019-08-22
     * @param deleteStatus 删除状态 是否删除 1删除 2未删除
     * @param userName 用户名
     * @param loginTime 登陆时间
     * @return Integer
     */
    @Select("<script>" +
            "  select count(1) from system_login_log " +
            "  where 1=1 and delete_status = #{deleteStatus} and user_name = #{userName} and login_time >= #{loginTime} " +
            "</script>"
            )
    Integer countByLoginTimeAndUserName(@Param("deleteStatus") Integer deleteStatus
            , @Param("userName") String userName, @Param("loginTime") String loginTime);

}
