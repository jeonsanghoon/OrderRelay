package com.them.orderrelay.domain.mongo.base.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDtoReq {
    /**
     * mongo id
     */
    private String id;
    /**
     * 업체코드
     */
    private String entpCd;
    /**
     * 매장코드
     */
    private String storeCd;

    /**
     * 배달대행사 코드
     */
    private String channelEntpCd;

    /**
     * 배달대행사에서 구분하는 매장코드(구분키)
     */
    private String channelStoreCd;

    private Boolean isUse = true;
}
