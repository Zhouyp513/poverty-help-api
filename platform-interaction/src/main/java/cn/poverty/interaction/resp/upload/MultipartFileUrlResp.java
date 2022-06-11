package cn.poverty.interaction.resp.upload;

import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 多文件查询Resp
 * @date 2020-07-10
 */
@Data
public class MultipartFileUrlResp implements Serializable {

    private static final long serialVersionUID = -8973899738444927838L;



    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件直接访问地址
     */
    private String url;

}
