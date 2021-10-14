package com.them.orderrelay.framework.restapi;

import org.springframework.http.HttpHeaders;

public interface RestApiHeader {
    String getServerUrl();
    HttpHeaders getHeader();
}


