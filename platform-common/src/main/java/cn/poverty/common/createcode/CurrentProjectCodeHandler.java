package cn.poverty.common.createcode;


import lombok.extern.slf4j.Slf4j;

/**
 * 项目代码生成器
 * @title: CreateCode.java
 
 * @date 2021-02-14
 */
@Slf4j
public class CurrentProjectCodeHandler {

    /**
     * 数据库配置->驱动类名
     */
    private final static String DIVER = "com.mysql.jdbc.Driver";

    /**
     * 数据库配置
     */
    public final static String DB_NAME = "poverty-help";

    /**
     * 数据库配置->连接URL
     */
    public final static String URL = "jdbc:mysql://42.193.17.167:3306/"+ DB_NAME +"?useUnicode=true&characterEncoding=utf-8&useSSL=false";

    /**
     * 删除标志字段
     */
    public final static String DB_USER = "poverty";

    /**
     * 密码
     */
    public final static String DB_PASSWORD = "123456";

    /**
     * 模块前缀名
     */
    public final static String MODULE_NAME = "platform";

    /**
     * 主要的包名 不带域名前缀(.cn和.com等)
     */
    public final static String MAIN_PACKAGE_NAME = "poverty";

    /**
     * 数据表配置 多张表用逗号隔开
     */
    public final static String TABLE_NAME = "trace_record";


    /**
     * 运行此方法，可以生成对应的数据表的实体
     * 
     * @date 2020/12/31
     * @param args 参数
     */
    public static void main(String[] args) {
        try {
            System.setProperty("DB_DIVER",DIVER);
            System.setProperty("DB_URL",URL);
            System.setProperty("DB_USER",DB_USER);
            System.setProperty("DB_PASSWORD",DB_PASSWORD);
            //创建mybatis
            String tableName = TABLE_NAME;
            //初始化项目路径
            CodeHandler.setProjectNameAndModuleName(MODULE_NAME,MAIN_PACKAGE_NAME);

            //拿到数据库表字段信息
            CodeHandler.readTableField(DB_NAME, tableName);
            //创建文件
            CodeHandler.createTableFile(tableName.toLowerCase());
        } catch (Exception e) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>生成项目代码失败,异常信息: {} , {} <<<<<<<<<<<<<<<<<<<",e.getMessage(),e);
        }
    }
}
