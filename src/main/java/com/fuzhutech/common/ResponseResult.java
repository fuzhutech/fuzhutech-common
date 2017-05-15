package com.fuzhutech.common;

//操作结果集
public class ResponseResult {

    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;

    private int status = FAILURE;
    private Object data = null;
    private String message = "";


    public ResponseResult() {
        //
    }

    public ResponseResult(int status) {
        this.status = status;
    }

    public ResponseResult(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    public ResponseResult(int status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
