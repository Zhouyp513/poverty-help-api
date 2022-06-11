package cn.poverty.common.utils;

import tk.mybatis.mapper.entity.Example;

/**
 * @author
 * @Title: CriteriaBuilder
 * @ProjectName poverty-help-api
 * @Description: 查询条件构建器
 * @date 2018/11/16 14:15
 */
public class CriteriaBuilder {



    /**
     * 设置固有的查询条件
     * @author
     * @date 2018/11/16 14:16
     * @param criteria
     */
    public static void rigidCriteria(Example.Criteria criteria){
        criteria.andEqualTo("deleteStatus",2);
    }
}
