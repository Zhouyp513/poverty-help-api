package cn.poverty.interaction.resp.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.*;
import java.time.LocalDateTime;

/**
 * @author
 * @packageName cn.poverty.interaction.resp.export
 * @Description: 角色信息Resp
 * @date 2021-04-12
 */
@Data
public class AuthRoleExportResp implements Serializable {


    private static final long serialVersionUID = 980951468115090984L;


    /**
     * 角色名称
     */
    @Excel(name = "角色名称", width = 10)
    private String roleName;

    /**
     * 角色描述
     */
    @Excel(name = "角色描述", width = 10)
    private String remark;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", format = "yyyy-MM-dd", width = 15)
    private LocalDateTime createTime;
}
