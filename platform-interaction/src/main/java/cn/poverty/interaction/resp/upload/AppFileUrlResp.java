package cn.poverty.interaction.resp.upload;

import lombok.Data;

import java.io.*;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: APP文件访问Resp
 * @date 2019-09-18
 */
@Data
public class AppFileUrlResp implements Serializable {


    private static final long serialVersionUID = -2658177002166773646L;


    /**
      * APP文件名称
      */
    private String appFileName;

    /**
     * APP文件直接访问地址
     */
    private String appFileAccessUrl;




}
