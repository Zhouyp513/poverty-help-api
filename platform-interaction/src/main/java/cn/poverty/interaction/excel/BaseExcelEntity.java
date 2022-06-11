package cn.poverty.interaction.excel;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author
 * @packageName cn.poverty.interaction.excel
 * @Description: 基础的导入和导出数据校验
 * @date 2021-04-27
 */
@Data
public class BaseExcelEntity implements IExcelDataModel, IExcelModel {

    /**
     * 行号
     */
    private Integer rowNum;

    /**
     * 错误消息
     */
    private String errorMsg;

    @Override
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }
}
