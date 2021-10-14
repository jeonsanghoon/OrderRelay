package com.them.orderrelay.framework.util.mail;

public enum MessageType {
    error("error", "에러"), warn("warn","경고"), info("info","정보");
    private String code;
    private String name;

    MessageType(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public String getCode(){
        return this.code;
    }

    public String getName(){
        return this.name;
    }
}
