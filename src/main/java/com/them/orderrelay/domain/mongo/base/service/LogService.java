package com.them.orderrelay.domain.mongo.base.service;

import com.them.orderrelay.domain.mongo.base.MongoBaseService;
import com.them.orderrelay.domain.mongo.base.dto.LogDto;
import com.them.orderrelay.domain.mongo.base.dto.LogDtoReq;

public interface LogService extends MongoBaseService<LogDtoReq, LogDto> {
    String errorLogInsert(String exMessage) throws Exception;
}
