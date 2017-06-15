package com.fuzhutech.common;

import java.util.HashMap;
import java.util.Map;

//操作结果集
public class ResponseResult {

    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;

    private int status;
    private String message;
    private Map<String, Object> data;

    public ResponseResult() {
        this(ResponseResult.SUCCESS);
    }

    public ResponseResult(int status) {
        this(status, "");
    }

    public ResponseResult(int status, String message) {
        this(status, message, null);
    }

    public ResponseResult(int status, Map<String, Object> data) {
        this(status, "", data);
    }

    public ResponseResult(int status, String message, Map<String, Object> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void putData(String key, Object value) {
        if (data == null) {
            data = new HashMap<String, Object>();
        }

        data.put(key, value);
    }

}
