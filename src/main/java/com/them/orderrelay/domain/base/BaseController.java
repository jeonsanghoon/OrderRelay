package com.them.orderrelay.domain.base;

import org.springframework.http.ResponseEntity;

public interface BaseController <TReq, T>{
    ResponseEntity<T> request(TReq reqParam) throws Exception;
}
