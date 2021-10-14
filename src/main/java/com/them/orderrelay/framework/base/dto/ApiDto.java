package com.them.orderrelay.framework.base.dto;

import com.them.orderrelay.framework.exception.ResponseType;
import lombok.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiDto {
    @Setter
    @Getter
    private String message;
    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private ResponseType responseType;

    public ApiDto(String message, String code) {
        this.message = message;
        this.code = code;
        this.responseType = ResponseType.Info;
    }

    public ApiDto(String message, String code, ResponseType responseType) {
        this.message = message;
        this.code = code;
        this.responseType = responseType;
    }
}
