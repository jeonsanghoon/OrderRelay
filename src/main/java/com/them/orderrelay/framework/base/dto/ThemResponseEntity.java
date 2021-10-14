package com.them.orderrelay.framework.base.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ThemResponseEntity<T> extends ResponseEntity<T> {

    public ThemResponseEntity(T body) {
        super(body, HttpStatus.OK);
    }

    public ThemResponseEntity(T body, HttpStatus status) {
        super(body, status);
    }
}
