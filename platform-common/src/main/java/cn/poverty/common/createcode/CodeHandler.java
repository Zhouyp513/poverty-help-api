package cn.poverty.common.createcode;

import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.common.StringTool;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Stream;


/**
 * 生成实体,Repository文件
 * @title: CreateCode.java
 
 * @date 2021-02-14
 */
@Slf4j
public class CodeHandler {

    /**
     * 删除标志字段
     */
    private final static String DELETE_STATUS = "delete_status";

    /**
     * 创建时间
     */
    private final static String CREATE_TIME = "create_time";

    /**
     * 删除时间
     */
    private final static String DELETE_TIME = "delete_time";

    /**
     * 更新时间
     */
    private final static String UPDATE_TIME = "update_time";

    /**
     * 主键
     */
    private final static String PKV = "PRI";

    /**
     * 删除标志字段
     */
    private final static String STRING = "String";

    /**
     * 删除标志字段
     */
    private final static String DATE = "LocalDateTime";

    /**
     * 删除标志字段
     */
    private final static String INTEGER = "Integer";

    /**
     * 删除标志字段
     */
    private final static String LONG = "Long";

    /**
     * 删除标志字段
     */
    private final static String BYTE = "byte";

    /**
     * 删除标志字段
     */
    private final static String BIG_DECIMAL = "BigDecimal";

    /**
     * 删除标志字段
     */
    private final static String ENUM = "enum";

    /**
     * 正括号
     */
    private final static String PARENT_BRACKET = "(";

    /**
     * 反括号
     */
    private final static String REVERSE_BRACKET = ")";

    /**
     * 逗号
     */
    private final static String COMMA_STRING = ",";

    /**
     * key=table_name  val=col的list
     */
    private static Map<String, List<Column>> columnHashMap = new HashMap<>();

    /**
     * 表名HashMap
     */
    private static Map<String, String> tableHashMap = new HashMap<>();

    /**
     * key=表名  val=col 只支持单主键
     */
    private static Map<String, Column> keyHashMap = new HashMap<>();

    /**
     * 当前项目所在的绝对路径
     */
    private final static String PROJECT_PATH = System.getProperty("user.dir");


    /**
     * 主要的包名
     */
    public static String MAIN_PACKAGE_NAME;

    /**
     * 域名后缀
     */
    public static String DOMAIN_SUFFIX = "cn.";


    /**
     * 实体类的包名
     */
    private static String ENTITY_PACKAGE = "";

    /**
     * 基础实体类的扩展文件路径
     */
    private static String ENTITY_PATH = "";

    /**
     * repository层路径
     */
    private static String REPOSITORY_PATH = "";

    /**
     * repository层包名
     */
    private static String REPOSITORY_PACKAGE = "";

    /**
     * 服务层路径
     */
    private static String SERVICE_PATH = "";

    /**
     * 服务层包名
     */
    private static String SERVICE_PACKAGE = "";

    /**
     * 服务实现层路径
     */
    private static String SERVICE_IMPL_PATH = "";

    /**
     * 服务实现层包名
     */
    private static String SERVICE_IMPL_PACKAGE = "";


    /**
     * 设置模块名称和项目名称信息
     * 
     * @date 2019-08-28
     * @param moduleName 模块名称
     * @param mainPackageName 主要包名
     * @return
     */
    public static void setProjectNameAndModuleName(String moduleName,String mainPackageName) {
        //主要包名
        CodeHandler.MAIN_PACKAGE_NAME = mainPackageName;

        String PACKAGE_PATH = StrUtil.replaceIgnoreCase(DOMAIN_SUFFIX+mainPackageName,".","/");

        //基础实体类的扩展文件路径
        CodeHandler.ENTITY_PATH = PROJECT_PATH + "/" +  moduleName + "-repository/src/main/java/"+ PACKAGE_PATH +"/repository/entity";
        //repository层
        CodeHandler.REPOSITORY_PATH = PROJECT_PATH + "/" +  moduleName + "-repository/src/main/java/"+ PACKAGE_PATH +"/repository/repository";
        //service层
        CodeHandler.SERVICE_PATH = PROJECT_PATH + "/" +  moduleName + "-service/src/main/java/"+ PACKAGE_PATH +"/service";
        //serviceImpl层
        CodeHandler.SERVICE_IMPL_PATH = PROJECT_PATH + "/" + moduleName + "-service/src/main/java/"+ PACKAGE_PATH +"/service/impl";

        //扩展包名
        CodeHandler.ENTITY_PACKAGE = DOMAIN_SUFFIX + MAIN_PACKAGE_NAME +".repository.entity";
        CodeHandler.REPOSITORY_PACKAGE = DOMAIN_SUFFIX + MAIN_PACKAGE_NAME +".repository.repository";
        CodeHandler.SERVICE_PACKAGE = DOMAIN_SUFFIX + MAIN_PACKAGE_NAME +".service";
        CodeHandler.SERVICE_IMPL_PACKAGE = DOMAIN_SUFFIX + MAIN_PACKAGE_NAME +".service.impl";
    }


    /**
     * 拿到数据库里面指定的表的所有的字段
     * 
     * @date 2021/3/29
     * @param dataBase 数据库名称
     * @param tableName 表名称
     * @return
     */
    public static void readTableField(String dataBase, String tableName) throws Exception {
        tableName = dropNull(tableName);
        String [] sz  = tableName.split(",");
        StringBuffer buffer = new StringBuffer();
        String tbs = null;
        if (tableName.length() != 0) {
            for (int i = 0; i < sz.length; i++) {
                String temp = sz[i].trim();
                if (temp.length() != 0) {
                    buffer.append(",").append("'").append(temp).append("'");
                }
            }
            tbs = buffer.toString();
            if (tbs.startsWith(COMMA_STRING)) {
                tbs = tbs.substring(1);
            }
        }
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String insql = "";
        if (tbs.length() != 0) {
            insql = " AND TABLE_NAME in (" + tbs + ") ";
        }
        String sqlTable = "select  TABLE_NAME,table_comment FROM information_schema.tables   " +
                " where TABLE_SCHEMA='" + dataBase + "' " + insql + " ORDER BY TABLE_NAME";
        ResultSet rsTable = statement.executeQuery(sqlTable);
        while (rsTable.next()) {
            String tn = rsTable.getString("TABLE_NAME").toLowerCase().trim();
            String comment = rsTable.getString("table_comment").trim();
            tableHashMap.put(tn, comment);
        }
        String sql = "select  TABLE_NAME,COLUMN_NAME,data_type,COLUMN_key,COLUMN_COMMENT, ORDINAL_POSITION,COLUMN_TYPE  FROM information_schema.`COLUMNS`  " +
                " where TABLE_SCHEMA='" + dataBase + "' " + insql + " ORDER BY TABLE_NAME,ORDINAL_POSITION";
        System.out.println(sql);
        //统计出列的总数
        Integer totalColumn = returnColumnTotalCount(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        //处理表结果查询的结果信息
        handleResultSet(resultSet, columnHashMap,totalColumn);
        statement.close();
        connection.close();
    }


    /**
     * 处理查询结果
     * 
     * @date 2021/3/29
     * @param resultSet resultSet
     * @param columnMap columnMap
     * @param totalCountColumn tot
     */
    private static void handleResultSet(ResultSet resultSet,Map<String, List<Column>> columnMap,Integer totalCountColumn)  throws Exception {
        String tblName = "";
        List<Column> columnList = new Vector<>();
        Integer num = 0;
        while (resultSet.next()) {
            num++;
            String tbName = resultSet.getString("TABLE_NAME").toLowerCase().trim();
            String columnName = resultSet.getString("COLUMN_NAME").toLowerCase().trim();
            String dataType = resultSet.getString("data_type").trim();
            String pk = resultSet.getString("COLUMN_key");
            String desc = resultSet.getString("COLUMN_COMMENT");
            String length = resultSet.getString("COLUMN_TYPE");
            if (desc == null) {
                desc = "";
            }
            Column column = new Column();
            if ("int".equals(dataType) || "long".equals(dataType) || "tinyint".equals(dataType)) {
                dataType = INTEGER;
            }else if("bigint".equals(dataType)){
                dataType = LONG;
            } else if ("date".equals(dataType)
                    || "datetime".equals(dataType)) {
                dataType = DATE;
            } else if ("float".equals(dataType) || "double".equals(dataType) || "decimal".equals(dataType)) {
                dataType = BIG_DECIMAL;
            } else if ("blob".equals(dataType)) {
                dataType = BYTE;
            } else if ("enum".equals(dataType)) {
                dataType = ENUM;
                column.setColumnName(columnName);
                column.setColumnNameDesc(desc);
                column.setDataType(dataType);
                column.setEnums(columnToEnums(length));
            } else {
                dataType = STRING;
            }
            if (!dataType.equals(ENUM)) {
                column.setColumnName(columnName);
                column.setColumnNameDesc(desc);
                column.setDataType(dataType);
                column.setColumnLength(columnLength(length));
            }

            //主键
            if (PKV.equals(pk)) {
                keyHashMap.put(tbName, column);
            }
            //不相等
            if (!tbName.equals(tblName)) {
                if (tblName.length() != 0) {
                    columnMap.put(tblName, columnList);
                    columnList = new Vector<>();
                    tblName = tbName;
                }
            }
            columnList.add(column);

            //如果字段循环完毕，则放进Map里面
            if (totalCountColumn.equals(num)) {
                columnMap.put(tbName, columnList);
            }
        }
        resultSet.close();
    }

    /**
     * 列数据变为枚举
     * 
     * @date 2021/3/29
     * @param  columnType 列数据类型
     * @return String[]
     */
    private static String[] columnToEnums(String columnType) {
        if(columnType == null || columnType.length() == 0 || columnType.indexOf(PARENT_BRACKET) == -1) {
            return null;
        }
        String s = columnType.substring(columnType.indexOf(PARENT_BRACKET) + 1);
        s = s.substring(0, s.indexOf(REVERSE_BRACKET));
        String[] enums = null;
        if (s.indexOf(COMMA_STRING) != -1) {
            enums = s.replace("\'", "").trim().split(",");
        }
        return enums;
    }

    /**
     * 需要被清除空的值
     * 
     * @date 2021/3/29
     * @param value
     * @return String
     */
    public static String dropNull(String value){
        if(null == value) {
            return "";
        }
        return value.trim();
    }


    /**
     * 创建表的对应的文件
     * 
     * @date 2021/3/29
     * @param tableNames 表名(逗号分割)
     */
    public static void createTableFile(String tableNames) throws Exception {
        String [] tableArray = tableNames.split(",");
        Stream.of(tableArray).forEach(item -> {
            try{
                item = item.toLowerCase().trim();
                //创建基础实体
                //creatBaseEntity(tb_name);
                //创建实体继承基础实体
                creatEntity(item);
                //创建DAO文件
                creatRepository(item);
                //创建Service
                //creatService(tb_name);
                //创建ServiceImpl
                //creatServiceImpl(tb_name);
            }catch (Exception e){
                log.error(">>>>>>>>>>>>>>>>>>>>>>创建数据库对应Java文件出现错误: {} ,{} <<<<<<<<<<<<<<<<",e.getMessage(),e);
            }
        });
    }

    /**
     * 拿到数据库的连接
     * 
     * @date 2021/3/29
     * @return
     */
    public static Connection getConnection() throws Exception {
        Class.forName(System.getProperty("DB_DIVER"));
        Connection con = DriverManager.getConnection(
                System.getProperty("DB_URL"),
                System.getProperty("DB_USER"),
                System.getProperty("DB_PASSWORD"));
        return con;
    }

    /**
     * 统计出表所有的字段的数量
     * 
     * @date 2021/3/29
     * @param sql SQL
     * @return Integer
     */
    public static Integer returnColumnTotalCount(String sql) throws Exception {
        Connection con = getConnection();
        Statement st = con.createStatement();
        sql = "select count(*) from (" + sql + ") as tt ";
        ResultSet resultSet = st.executeQuery(sql);
        resultSet.next();
        return resultSet.getInt(1);
    }

    /**
     * 创建实体类
     * @param tableName
     * @return String
     * @throws Exception
     */
    public static String creatEntity(String tableName) throws Exception {
        StringBuffer contentBuffer = new StringBuffer();
        String className = upFirst(tableName);
        contentBuffer.append("package " + ENTITY_PACKAGE.trim() + ";\n\n");

        String baseEntityImport = "import "+DOMAIN_SUFFIX + MAIN_PACKAGE_NAME;
        contentBuffer.append(baseEntityImport).append(".common.entity.BaseEntity;\n");
        contentBuffer.append("import java.math.BigDecimal;\n");
        contentBuffer.append("import java.util.Date;\n");
        contentBuffer.append(baseEntityImport).append(".common.utils.spring.SnowflakeIdWorker;\n");
        contentBuffer.append("import lombok.Data;\n");
        contentBuffer.append("import javax.persistence.*;\n");
        contentBuffer.append("import java.io.Serializable;\n\n");
        contentBuffer.append("@Table(name = \""+tableName+"\")\n");
        contentBuffer.append("@Entity\n");
        contentBuffer.append("@Data\n");
        contentBuffer.append("public class " + className + " extends BaseEntity implements Serializable  {\n\n");
        contentBuffer.append("\tprivate static final long serialVersionUID = 1L;\n\n");
        List<Column> columnList = columnHashMap.get(tableName);
        columnList.forEach(item -> {
            List<String> notNeedGenColumnList = Lists.newArrayList();
            notNeedGenColumnList.add("delete_status");
            notNeedGenColumnList.add("id");
            notNeedGenColumnList.add("create_time");
            notNeedGenColumnList.add("update_time");
            notNeedGenColumnList.add("operator_id");
            Long notNeedGenCount = notNeedGenColumnList.stream().filter(n1 -> n1.equalsIgnoreCase(item.columnName)).count();
            if(notNeedGenCount.compareTo(Long.valueOf(0)) == 0){
                String businessKey  = tableName+"_id";
                if(businessKey.equalsIgnoreCase(item.columnName)){
                    //此处生成注释
                    contentBuffer.append("\t/**\n\t * " + item.columnNameDesc + "\n \t */\n");
                    contentBuffer.append("\t@Column(name = \"" +item.columnName + "\") \n");
                    contentBuffer.append("\tprivate " + item.dataType + "  " + camelCaseName(item.columnName) + " = SnowflakeIdWorker.uniqueSequenceStr(); \n\n");
                }else{
                    //此处生成注释
                    contentBuffer.append("\t/**\n\t * " + item.columnNameDesc + "\n \t */\n");
                    contentBuffer.append("\t@Column(name = \"" +item.columnName + "\") \n");
                    contentBuffer.append("\tprivate " + item.dataType + "  " + camelCaseName(item.columnName) + "; \n\n");
                }
            }
        });
        contentBuffer.append("}\n");
        //创建文件
        createUtfFile(ENTITY_PATH.trim() + "/" + className + ".java", contentBuffer.toString());
        return contentBuffer.toString();
    }


    /**
     * 创建Repository
     * 
     * @date 2021/3/29
     * @param tableName 表名
     * @return String
     */
    public static String creatRepository(String tableName) throws Exception {
        StringBuffer sb = new StringBuffer();
        String baseEntityImport = "import "+DOMAIN_SUFFIX + MAIN_PACKAGE_NAME;
        String className = upFirst(tableName);
        sb.append("package " + REPOSITORY_PACKAGE + ";\n");
        sb.append(baseEntityImport).append(".common.config.BaseRepository;\n");
        sb.append("import " + ENTITY_PACKAGE + "." + className + ";\n");
        //生@Mapper注解
        sb.append("import org.apache.ibatis.annotations.Mapper;\n");
        sb.append("\n");
        sb.append("@Mapper\n");
        sb.append("public interface " + className + "Repository extends BaseRepository<"+className+">{\n");
        sb.append("}");
        createUtfFile(REPOSITORY_PATH.trim() + "/" + className + "Repository.java", sb.toString());
        return sb.toString();
    }
    /**
     * 创建service
     *
     * @return
     */
    public static String creatService(String tableName) throws Exception {
        StringBuffer sb = new StringBuffer();
        String className = upFirst(tableName);
        Column column = keyHashMap.get(tableName);
        sb.append("package " + SERVICE_PACKAGE + ";\n");
        sb.append("\n");

        sb.append("public interface " + className + "Service {\n");
        sb.append("}");
        createUtfFile(SERVICE_PATH.trim() + "/" + className + "Service.java", sb.toString());
        return sb.toString();

    }

    /**
     * 创建服务的实现层
     * 
     * @date 2021/3/29
     * @param tableName 表名
     * @return String
     */
    public static String creatServiceImpl(String tableName) throws Exception {
        StringBuffer sb = new StringBuffer();
        String className = upFirst(tableName);
        Column pk = keyHashMap.get(tableName);
        sb.append("package " + SERVICE_IMPL_PACKAGE + ";\n");
        sb.append("\n");
        sb.append("import "+ SERVICE_PACKAGE +"."+className+"Service;\n");
        sb.append("import "+"lombok.extern.slf4j.Slf4j;\n");
        sb.append("import "+"org.springframework.stereotype.Service;\n\n");
        sb.append("@Service\n")  ;
        sb.append("@Slf4j\n")  ;
        sb.append("public class " + className + "ServiceImpl implements "+className+"Service {\n");
        sb.append("}");
        createUtfFile(SERVICE_IMPL_PATH.trim() + "/" + className + "ServiceImpl.java", sb.toString());
        return sb.toString();
    }

    /**
     *
     * 创建UTF文件
     * 
     * @date 2021/3/29
     * @param filePath 文件路径
     * @param content 文件内容
     * @return
     */
    public static void createUtfFile(String filePath, String content) throws Exception {
        String path = filePath.substring(0, filePath.lastIndexOf("/"));
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
        out.write(content);
        out.close();
    }

    /**
     * 拿到列的长度
     * 
     * @date 2021/3/29
     * @param columnType
     * @return
     */
    private static Integer columnLength(String columnType) {
        int ret = 1000;
        if (CheckParam.isNull(columnType) ||
                columnType.indexOf(PARENT_BRACKET) == -1) {
            return ret;
        }
        String result = columnType.substring(columnType.indexOf(PARENT_BRACKET) + 1);
        result = result.substring(0, result.indexOf(REVERSE_BRACKET));
        if (result.indexOf(COMMA_STRING) != -1) {
            result = result.substring(0, result.indexOf(COMMA_STRING));
        }
        return Integer.valueOf(result);
    }

    /**
     * 首字母转驼峰，下划线转驼峰
     * 
     * @date 2021/3/29
     * @param str 需要被转换的字段
     * @return String
     */
    public static String upFirst(String str) {
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        str = StringTool.camel(new StringBuffer(str)).toString();
        return str;
    }

    /**
     * 转换为驼峰下划线
     * 
     * @date 2021/3/29
     * @param underscoreName 需要被转换的下划线字段字段
     * @return String
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }
}
