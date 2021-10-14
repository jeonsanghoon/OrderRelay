package com.them.orderrelay.domain.mongo.base.service;

import com.them.orderrelay.domain.mongo.base.MongoBaseService;
import com.them.orderrelay.domain.mongo.base.dto.UserDto;
import com.them.orderrelay.domain.mongo.base.dto.UserDtoReq;
import com.them.orderrelay.framework.exception.UserException;

import java.util.List;

public interface UserService extends MongoBaseService<UserDtoReq, UserDto> {
    List<UserDto> getUserList(UserDtoReq req) throws UserException;
}

