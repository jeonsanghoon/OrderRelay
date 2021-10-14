package com.them.orderrelay.domain.mongo.base.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginDto extends TokenDto {
    /**
     * 매장정보
     */
    private List<LoginStoreDto> storeList;
}
