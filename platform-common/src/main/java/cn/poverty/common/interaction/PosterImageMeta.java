package cn.poverty.common.interaction;

import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poverty.common.interaction
 * @Description: 海报基元信息(可以用作缓存)
 * @date 2021-02-26
 */
@Data
public class PosterImageMeta extends ImageMeta implements Serializable {

    private static final long serialVersionUID = -8931942178137352087L;


    /**
      * 海报访问地址
      */
    private String imageUrl;

}
