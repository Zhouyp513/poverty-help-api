package cn.poverty.common.enums.paper;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 
 * @packageName cn.poverty.common.enums
 * @Description: 文件类型枚举
 * @date 2021-02-16
 */
@Getter
@AllArgsConstructor
public enum PaperTypeEnum {

    /**
      * 文件类型-图片
      */
    IMAGE ("IMAGE","图片"),

    /**
     * 文件类型-视频
     */
    VIDEO("VIDEO","视频");

    /**
     * 状态码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
