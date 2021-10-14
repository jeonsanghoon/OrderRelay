package com.them.orderrelay.domain.mongo.base.service;

import com.them.orderrelay.domain.mongo.base.MongoBaseService;
import com.them.orderrelay.domain.mongo.base.dto.StoreDto;
import com.them.orderrelay.domain.mongo.base.dto.StoreDtoReq;

public interface StoreService extends MongoBaseService<StoreDtoReq, StoreDto> {
    <T> String StoreUpdateDepositAmt(T dto, String dpstBalAmt) throws Exception;
}