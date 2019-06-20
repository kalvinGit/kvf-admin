package com.kalvin.kvf.common.exception;

/**
 * 自定义异常类
 */
public class KvfException extends RuntimeException {

    private static final long serialVersionUID = 3844100562400725986L;
    private Integer errorCode;
    private String msg;

    public KvfException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public KvfException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
