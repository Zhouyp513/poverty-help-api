package cn.poverty.common.interaction;

import lombok.Data;

import java.io.*;

/**
 
 * @packageName cn.poverty.common.interaction
 * @Description:
 * @date 2021-01-26
 */
@Data
public class WxPhoneNumberWrap implements Serializable {

    private String phoneNumber;
    private String purePhoneNumber;
    private int countryCode;
    private String weixinWaterMark;
    private WaterMark watermark;
}
