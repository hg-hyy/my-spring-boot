package com.hg.hyy.entity;

public class Msg {
    
    private String msg = "";
    private Integer code = 1000;
    private String data = "";

    public Msg(String msg, Integer code, String data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg  = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
