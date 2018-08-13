package com.slowinski.andrzej.qrevents;

public class QRCode {
    private int id;
    private String code;
    private int statusCheck;

    public QRCode(){}

    public QRCode(int id, String code, int status){
        this.id = id;
        this.code = code;
        this.statusCheck = status;
    }

    public QRCode(String code, int status){
        this.code = code;
        this.statusCheck = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatusCheck() {
        return statusCheck;
    }

    public void setStatusCheck(int statusCheck) {
        this.statusCheck = statusCheck;
    }
}
