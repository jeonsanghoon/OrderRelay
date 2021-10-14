package com.them.orderrelay.domain.mongo.base;

import java.util.List;

public interface MongoBaseService <TReq, T>{
    List<T> getList(TReq reqParam) throws Exception;
    T getData(TReq req) throws Exception;
    String insert(T data) throws Exception;
    String update(T data) throws Exception;
    String delete(TReq req) throws Exception;
}
