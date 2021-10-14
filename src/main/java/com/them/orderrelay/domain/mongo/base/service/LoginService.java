package com.them.orderrelay.domain.mongo.base.service;

import com.them.orderrelay.domain.mongo.base.MongoBaseService;
import com.them.orderrelay.domain.mongo.base.dto.*;
import com.them.orderrelay.framework.base.dto.ResDto;

public interface LoginService {
    LoginDto login(UserDtoReq reqParam) throws Exception;
    LoginDto testLogin(UserDtoReq reqParam) throws Exception;
    ResDto logout(UserDtoReq reqParam) throws Exception;
    TokenDto getToken(UserDtoReq reqParam) throws Exception;
    TokenDto getRefreshTokenNotStore(RefreshTokenDto reqParam) throws Exception;
    LoginDto getRefreshToken(RefreshTokenDto reqParam) throws Exception;
}

