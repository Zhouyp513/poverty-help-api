package cn.poverty.common.exception;

import cn.poverty.common.enums.ErrorCode;

/**
 
 * @time 2018/9/29
 * @description
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = -234801665080134237L;


    private String errorCode;

    private String errorMessage;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getCode()+":"+errorCode.getMessage());
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
