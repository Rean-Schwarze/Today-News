package com.rean.todaynews;

import java.util.List;

public class TypeResponse {
    private int code;
    private String msg;
    private List<TypeInfo> data;

    public TypeResponse(int code, String msg, List<TypeInfo> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<TypeInfo> getData() {
        return data;
    }

    public void setData(List<TypeInfo> data) {
        this.data = data;
    }
}
