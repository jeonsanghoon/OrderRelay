package com.them.orderrelay.domain.mongo.base;


import com.them.orderrelay.framework.base.dto.ThemResponseEntity;

import java.util.List;

public interface MongoBaseController<TReq, T> {
    ThemResponseEntity<List<T>> getList(TReq reqParam) throws Exception;
    ThemResponseEntity<T> getData(TReq reqParam) throws Exception;
    ThemResponseEntity<String> insert(T data) throws Exception;
    ThemResponseEntity<String> update(T data) throws Exception;
    ThemResponseEntity<String> delete(TReq param) throws Exception;
}
