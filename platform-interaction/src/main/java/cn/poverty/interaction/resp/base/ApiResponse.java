package cn.poverty.interaction.resp.base;


import cn.poverty.common.enums.ErrorCode;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Api请求返回实体
 
 * @time 2018/9/29
 * @description
 */
@Data
public class ApiResponse<E> implements Serializable {

    private static final long serialVersionUID = -8099598967725340002L;

    /**
     * 请求序列号
     */
    //@ApiModelProperty(value = "请求流水号",required = true,notes = "请求流水号",example = "931189104492675072")
    private String requestSeqNo;

    /**
     * 错误码
     */
    //@ApiModelProperty(value = "错误码",required = true,notes = "错误码",example = "000000")
    private String code;

    /**
     * 错误信息
     */
    //@ApiModelProperty(value = "错误原因",required = true,notes = "错误原因",example = "登陆成功")
    private String message;

    /**
     * 接口返回时间
     */
    //@ApiModelProperty(value = "接口返回时间",required = true,notes = "接口返回时间",example = "yyyy-MM-dd HH:mm:ss格式，如2017-11-17 00:15:12")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime responseTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());

    /**
     * 接口响应时间
     */
    //@ApiModelProperty(value = "接口响应时间",required = true,notes = "接口响应时间")
    private String spendTime;

    /**
     * 返回的数据
     */
    private E data;

    public ApiResponse() {
    }

    public ApiResponse(String requestSeqNo, String code, String message, E data) {
        this.requestSeqNo = requestSeqNo;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse error() {
        return error(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "未知异常，请联系管理员");
    }

    public static ApiResponse error(String msg) {
        return error(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), msg);
    }

    public static ApiResponse error(String code, String msg) {
        ApiResponse r = new ApiResponse();
        r.setCode(code);
        r.setMessage(msg);
        return r;
    }

    public static ApiResponse success() {
        ApiResponse r = new ApiResponse();
        r.setCode(ErrorCode.SUCCESS.getCode());
        r.setMessage(ErrorCode.SUCCESS.getMessage());
        return r;
    }

    public static ApiResponse unSuccess(ErrorCode errorCode) {
        ApiResponse r = new ApiResponse();
        r.setCode(errorCode.getCode());
        r.setMessage(errorCode.getMessage());
        return r;
    }

    public static ApiResponse ok(Object data) {
        ApiResponse r = new ApiResponse();
        r.setMessage("操作成功");
        r.setData(data);
        return r;
    }

}
