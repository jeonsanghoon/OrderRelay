package com.them.orderrelay.framework.exception;

import com.them.orderrelay.framework.base.dto.ResDto;
import com.them.orderrelay.framework.util.Global;

public class UserException extends Exception {
    private String code;
    private ResponseType responseType;


    public UserException(String code) {
        super(Global.getMessageInfo().getMessage(code));
        this.code= code;
        this.responseType = ResponseType.Info;
    }

    public UserException(String code, ResponseType exceptionType) {
        super(Global.getMessageInfo().getMessage(code));
        this.code= code;
        this.responseType = exceptionType;
    }

    public UserException(String errorCode,String message) {
        super(message);
        this.code = errorCode;
        this.responseType = ResponseType.Info;
    }

    public UserException(String errorCode,String message, ResponseType exceptionType) {
        super(message);
        this.code = errorCode;
        this.responseType = exceptionType;
    }

    public UserException(ResDto resDto, ResponseType exceptionType) {
        super(resDto.getResMsg());
        this.code = resDto.getResCd();
        this.responseType = exceptionType;
    }

    public UserException(Throwable cause) {
        super(cause);
        this.code = "-1";
        this.responseType = ResponseType.Error;
    }

    public String getCode() {
        return this.code;
    }

    public ResponseType getResponseType() { return this.responseType; }
}
