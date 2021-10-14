package com.them.orderrelay.domain.mongo.base.controller;

import com.them.orderrelay.domain.mongo.base.dto.RefreshTokenDto;
import com.them.orderrelay.domain.mongo.base.dto.UserDtoReq;
import com.them.orderrelay.domain.mongo.base.service.LoginService;
import com.them.orderrelay.framework.base.dto.ResDto;
import com.them.orderrelay.framework.base.dto.ThemResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = { "로그인" }, value = "LoginController")
@RestController
@Slf4j
@RequestMapping("/v1/orderRelay/")
public class LoginController {
    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }
    @ApiOperation(value = "로그인", notes = "로그인입니다.")
    @PostMapping("login")
    public ThemResponseEntity<ResDto> login(@RequestBody UserDtoReq reqParam) throws Exception {
        return new ThemResponseEntity(service.login(reqParam), HttpStatus.OK) ;
    }

    @ApiOperation(value = "테스트로그인", notes = "테스트로그인입니다.")
    @PostMapping("testLogin")
    public ThemResponseEntity<ResDto> testLogin(@RequestBody UserDtoReq reqParam) throws Exception {
        return new ThemResponseEntity(service.testLogin(reqParam), HttpStatus.OK) ;
    }

    @ApiOperation(value = "토큰발급(민료일지정)", notes = "토큰발급입니다.")
    @PostMapping("oauth/token")
    public ThemResponseEntity<ResDto> getToken(@RequestBody UserDtoReq reqParam) throws Exception {
        return new ThemResponseEntity(service.getToken(reqParam), HttpStatus.OK) ;
    }

    @ApiOperation(value = "토큰발급(만료일지정)", notes = "토큰발급입니다.")
    @PostMapping("oauth/refreshtoken")
    public ThemResponseEntity<ResDto> getRefreshToken(@RequestBody RefreshTokenDto reqParam) throws Exception {
        return new ThemResponseEntity(service.getRefreshTokenNotStore(reqParam), HttpStatus.OK) ;
    }

    @ApiOperation(value = "리플레시토큰으로 재발급", notes = "리플레시토큰으로 재발급합니다.")
    @PostMapping("getReToken")
    public ThemResponseEntity<ResDto> getRetoken (@RequestBody RefreshTokenDto reqParam) throws Exception {
        return new ThemResponseEntity(service.getRefreshToken(reqParam), HttpStatus.OK) ;
    }
}

