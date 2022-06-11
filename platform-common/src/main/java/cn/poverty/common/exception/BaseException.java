package cn.poverty.common.exception;

import lombok.Data;

import java.io.Serializable;

/**
 
 * @packageName cn.poverty.common.enums
 * @Description: 自定义异常
 * @date 2021-02-16
 */
@Data
public class BaseException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;


    private String msg;

    private int code = 500;

    public BaseException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BaseException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public BaseException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public BaseException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}
