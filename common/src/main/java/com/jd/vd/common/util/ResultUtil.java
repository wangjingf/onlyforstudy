package com.jd.vd.common.util;

public class ResultUtil {
    private int code;
    private String msg;
    private Object data;
    public static ResultUtil SUCCESS = new ResultUtil(0, "success", null);

    public static ResultUtil success(Object data) {
        return new ResultUtil(0, "success", data);
    }

    public ResultUtil(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultUtil getInstance(int code, String msg, Object data) {
        return new ResultUtil(code, msg, data);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultUtil [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }
}

