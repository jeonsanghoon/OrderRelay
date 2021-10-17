package com.them.orderrelay.domain.base;

import lombok.Getter;
import lombok.Setter;

public abstract class BasePosReqDto {
    @Getter
    @Setter
    private String methodName = "orderRequest";
    @Getter
    @Setter
    private String routeKey ;
}
