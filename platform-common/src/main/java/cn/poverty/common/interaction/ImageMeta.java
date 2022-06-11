package cn.poverty.common.interaction;

import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poverty.common.interaction
 * @Description: 图片基元信息
 * @date 2021-02-24
 */
@Data
public class ImageMeta implements Serializable {

    private static final long serialVersionUID = -621189079982316207L;


    /**
      * 宽度
      */
    private Integer width;

    /**
     * 宽度
     */
    private Integer height;

}
