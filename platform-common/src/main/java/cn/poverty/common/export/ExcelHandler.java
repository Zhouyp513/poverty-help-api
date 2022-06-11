package cn.poverty.common.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

/**

 * @packageName cn.poverty.common.export
 * @Description: Excel导出构建工具类
 * @date 2021-03-22
 */
public class ExcelHandler {

    /**
     * 导出Excel
     
     * @date 2019/1/15 9:38
     * @param dataList 数据集合
     * @param title Excel的Title
     * @param sheetName 表格名称
     * @param clz 目标Class
     * @param fileName 文件名称
     * @param isCreateHeader 是否创建表头
     * @return
     */
    public static<T> void exportExcel(List<T> dataList, String title,
                                      String sheetName, Class<?> clz, String fileName,
                                      boolean isCreateHeader, HttpServletResponse response) {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(dataList, clz, fileName, response, exportParams);
    }


    /**
     * 导出Excel
     
     * @date 2019/1/15 9:38
     * @param dataList 数据集合
     * @param title Excel的Title
     * @param sheetName 表格名称
     * @param clz 目标Class
     * @param fileName 文件名称
     * @param isCreateHeader 是否创建表头
     * @param dictHandler 字典处理
     * @return
     */
    public static<T> void exportExcel(List<T> dataList, String title,
                                      String sheetName, Class<?> clz, String fileName,
                                      boolean isCreateHeader,
                                      HttpServletResponse response,
                                      IExcelDictHandler dictHandler) {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        if(null != dictHandler){
            exportParams.setDictHandler(dictHandler);
        }
        defaultExport(dataList, clz, fileName, response, exportParams);
    }

    private ExcelHandler() {}


    /**
     * 下载Excel(默认)
     * 
     * @date 2021/7/22
     * @param fileName 文件名称
     * @param response HttpServletResponse
     * @param workbook workbook
     */
    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new  RuntimeException(e);
        }
    }

    /**
     * 导出Excel(默认)
     * 
     * @date 2021/7/22
     * @param dataList 数据集合
     * @param clz 类名
     * @param fileName 文件名称
     * @param response HttpServletResponse
     * @param exportParams 导出参数
     */
    private static<T> void defaultExport(List<T> dataList,
                                         Class<?> clz,
                                         String fileName,
                                         HttpServletResponse response,
                                         ExportParams exportParams) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, clz, dataList);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    /**
     * 导出Excel
     * 
     * @date 2021/7/22
     * @param dataList 数据集合
     * @param title excel标题
     * @param sheetName 表格名称
     * @param clz 类名
     * @param fileName 文件名称
     * @param response HttpServletResponse
     */
    public static<T> void exportExcel(List<T> dataList,
                                      String title,
                                      String sheetName,
                                      Class<?> clz,
                                      String fileName,
                                      HttpServletResponse response) {
        defaultExport(dataList, clz, fileName, response, new ExportParams(title, sheetName));
    }

    /**
     * 导出Excel(默认)
     * 
     * @date 2021/7/22
     * @param dataList 数据集合
     * @param fileName 文件名称
     * @param response HttpServletResponse
     */
    private static void defaultExport(List<Map<String, Object>> dataList, String fileName, HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(dataList, ExcelType.HSSF);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    /**
      * 导出Excel
      * 
      * @date 2021/7/22
      * @param dataList 数据集合
      * @param fileName 文件名称
      * @param response HttpServletResponse
      */
    public static void exportExcel(List<Map<String, Object>> dataList, String fileName, HttpServletResponse response) {
        defaultExport(dataList, fileName, response);
    }


    /**
     * 导入Excel
     * 
     * @date 2021/4/21
     * @param filePath
     * @param titleRows
     * @param headerRows
     * @param clz
     * @return <T>
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> clz) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        try {
            return ExcelImportUtil.importExcel(new File(filePath), clz, params);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 导入Excel
     * 
     * @date 2021/4/21
     * @param file 文件
     * @param titleRows
     * @param headerRows
     * @param clz
     * @return <T>
     */
    public static <T> List<T> importExcel(File file,
                                          Integer titleRows,
                                          Integer headerRows,
                                          Class<T> clz,
                                          IExcelDictHandler dictHandler) {
        if (file == null) {
            return null;
        }

        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedVerify(true);
        if(null != dictHandler){
            params.setDictHandler(dictHandler);
        }
        try {
            return ExcelImportUtil.importExcel(file, clz, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 导入Excel
     * 
     * @date 2021/4/21
     * @param file 文件
     * @param titleRows
     * @param headerRows
     * @param clz
     * @return <T>
     */
    public static <T> List<T> importExcel(MultipartFile file,
                                          Integer titleRows,
                                          Integer headerRows,
                                          Class<T> clz,
                                          IExcelDictHandler dictHandler) {
        if (file == null) {
            return null;
        }

        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedVerify(true);
        if(null != dictHandler){
            params.setDictHandler(dictHandler);
        }
        try {
            return ExcelImportUtil.importExcel(file.getInputStream(), clz, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 导入Excel
     * 
     * @date 2021/4/21
     * @param file 文件
     * @param titleRows
     * @param headerRows
     * @param clz
     * @return <T>
     */
    public static <T> ExcelImportResult<T> importExcelMore(MultipartFile file,
                                                           Integer titleRows,
                                                           Integer headerRows,
                                                           Class<T> clz,
                                                           IExcelDictHandler dictHandler) {
        if (file == null) {
            return null;
        }

        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedVerify(true);
        if(null != dictHandler){
            params.setDictHandler(dictHandler);
        }
        //此处可以设置自定义的处理器
        //params.setVerifyHandler();
        try {
            ExcelImportResult<T> importResult =
                    ExcelImportUtil.importExcelMore(file.getInputStream(), clz, params);
            return importResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
