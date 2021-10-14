package com.them.orderrelay.domain.mongo.base.controller;

import com.them.orderrelay.domain.mongo.base.MongoBaseController;
import com.them.orderrelay.domain.mongo.base.dto.StoreDto;
import com.them.orderrelay.domain.mongo.base.dto.StoreDtoReq;
import com.them.orderrelay.domain.mongo.base.service.StoreService;
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
@RequestMapping("/v1/dlvyStore/")
public class StoreController implements MongoBaseController<StoreDtoReq, StoreDto> {

    private final StoreService service;

    public StoreController(StoreService service) {
        this.service = service;
    }

    @ApiOperation(value = "배달매장 조회", notes = "배달매장 조회입니다.")
    @PostMapping("getList")
    @Override
    public ThemResponseEntity<List<StoreDto>> getList(@RequestBody StoreDtoReq reqParam) throws Exception {
        return new ThemResponseEntity(service.getList(reqParam), HttpStatus.OK) ;
    }

    @ApiOperation(value = "배달매장 조회(단건)", notes = "배달매장 조회(단건)입니다.")
    @PostMapping("getData")
    @Override
    public ThemResponseEntity<StoreDto> getData(@RequestBody StoreDtoReq reqParam) throws Exception {
        return new ThemResponseEntity(service.getData(reqParam), HttpStatus.OK) ;
    }

    @ApiOperation(value = "배달매장 추가", notes = "배달매장 추가입니다.")
    @PostMapping("insert")
    @Override
    public ThemResponseEntity<String> insert(@RequestBody StoreDto data) throws Exception {
        return new ThemResponseEntity(service.insert(data), HttpStatus.OK) ;
    }

    @ApiOperation(value = "배달매장 수정", notes = "배달매장 수정입니다.")
    @PostMapping("update")
    @Override
    public ThemResponseEntity<String> update(@RequestBody StoreDto data) throws Exception {
        return new ThemResponseEntity(service.update(data), HttpStatus.OK) ;
    }

    @ApiOperation(value = "배달매장 삭제", notes = "배달매장 삭제입니다.")
    @PostMapping("delete")
    @Override
    public ThemResponseEntity<String> delete(@RequestBody StoreDtoReq param) throws Exception {
        return new ThemResponseEntity(service.delete(param), HttpStatus.OK) ;
    }
}
