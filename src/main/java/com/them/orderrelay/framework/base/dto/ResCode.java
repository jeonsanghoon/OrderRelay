package com.them.orderrelay.framework.base.dto;

public enum ResCode {
    ok("1"), fail("0");

    private String resCode ="";
    ResCode(String resCode) {
        this.resCode = resCode;
    }
    @Override
    public String toString() {
        return this.resCode;
    }
}
