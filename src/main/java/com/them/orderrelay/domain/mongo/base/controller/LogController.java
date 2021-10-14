package com.them.orderrelay.domain.mongo.base.controller;

import com.them.orderrelay.domain.mongo.base.MongoBaseController;
import com.them.orderrelay.domain.mongo.base.dto.LogDto;
import com.them.orderrelay.domain.mongo.base.dto.LogDtoReq;
import com.them.orderrelay.domain.mongo.base.service.LogService;
import com.them.orderrelay.framework.base.dto.ThemResponseEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = { "몽고DB - 기본정보" }, value = "MongoDB")
@Slf4j
@RestController
@RequestMapping("/v1/orderRelay/log/")
public class LogController implements MongoBaseController<LogDtoReq, LogDto> {
    private final LogService service;

    public LogController(LogService service) {
        this.service = service;
    }

    @ApiOperation(value = "로그 조회", notes = "로그 조회입니다.")
    @PostMapping("getList")
    @Override
    public ThemResponseEntity<List<LogDto>> getList(@RequestBody LogDtoReq reqParam) throws Exception {
        return new ThemResponseEntity(service.getList(reqParam), HttpStatus.OK) ;
    }

    @ApiOperation(value = "로그 조회", notes = "로그(단건) 조회입니다.")
    @PostMapping("getData")
    @Override
    public ThemResponseEntity<LogDto> getData(@RequestBody LogDtoReq reqParam) throws Exception {
        return new ThemResponseEntity(service.getData(reqParam), HttpStatus.OK) ;
    }

    @ApiOperation(value = "로그 추가", notes = "로그 추가입니다.")
    @PostMapping("insert")
    @Override
    public ThemResponseEntity<String> insert(@RequestBody LogDto reqParam) throws Exception {
        return new ThemResponseEntity(service.insert(reqParam), HttpStatus.OK) ;
    }

    @ApiOperation(value = "로그 수정", notes = "로그 수정입니다.")
    @PostMapping("update")
    @Override
    public ThemResponseEntity<String> update(@RequestBody LogDto reqParam) throws Exception {
        return new ThemResponseEntity(service.update(reqParam), HttpStatus.OK) ;
    }

    @ApiOperation(value = "로그 삭제", notes = "로그 삭제입니다.")
    @PostMapping("delete")
    @Override
    public ThemResponseEntity<String> delete(@RequestBody LogDtoReq param) throws Exception {
        return new ThemResponseEntity(service.delete(param), HttpStatus.OK) ;
    }
}
