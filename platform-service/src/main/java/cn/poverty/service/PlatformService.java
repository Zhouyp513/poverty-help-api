package cn.poverty.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 管理平台操作Service
 * @date 2020-06-11
 */
public interface PlatformService {




    /**
     * 从配置文件读取配置编辑器配置信息
     *
     * @date 2021/6/8
     * @return java.util.List
     */
    JSONObject editorConfig();

    /**
     * 生成雪花算法ID
     *
     * @date 2021/5/10
     * @return String
     */
    String snowflakeId();


    /**
     * 当前服务器时间
     *
     * @date 2021/5/10
     * @return String
     */
    Long currenLongTime();



}
