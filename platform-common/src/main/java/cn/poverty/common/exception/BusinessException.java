package cn.poverty.common.exception;

import lombok.Data;

/**
 * 系统业务异常类
 
 * @time 2018/9/29
 * @description 系统业务异常类
 */
@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1656492381432429706L;

    private String code;

    private String message;

    private Object data;

    public BusinessException(String errorCode, String errorMessage) {
        super(errorCode +":"+errorMessage);
        this.code = errorCode;
        this.message = errorMessage;
    }

    public BusinessException(String errorCode, String errorMessage,Object data) {
        super(errorCode +":"+errorMessage);
        this.code = errorCode;
        this.message = errorMessage;
        this.data = data;
    }
}
