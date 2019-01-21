package com.bestotc.exception;


/**
 * 基础异常类
 * Created by xuejingtao
 */
public class ApiOtcException extends RuntimeException {

    private int code;
    private String errorCode;


    public ApiOtcException(int code) {
        super();
        this.code = code;
    }
    public ApiOtcException(int code, String message) {
        super(message);
        this.code = code;
        this.errorCode = "";
    }

    public ApiOtcException(String message) {
        super(message);
        this.code = 500;
    }

    public ApiOtcException(int code, String errorCode, String message) {
        super(message);
        this.code = code;
        this.errorCode = errorCode;
    }

    public int getCode() {
        return code;
    }
}
