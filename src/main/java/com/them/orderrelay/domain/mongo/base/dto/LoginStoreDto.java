package com.them.orderrelay.domain.mongo.base.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Store")
@Getter
@Setter
public class LoginStoreDto {
    /**
     * 업체코드
     */
    private String entpCd;
    /**
     * 업체명
     */
    private String entpNm;
    /**
     * 매장코드
     */
    private String storeCd;
    /**
     * 매장명
     */
    private String storeNm;
    /**
     * 포스코드
     */
    private String posCd;
    /**
     * 대행사 코드
     */
    private String channelEntpCd;
    /**
     * 대행사명
     */
    private String dlvEntpNm;

    /**
     * 대행사 매장 구분코드
     */
    private String channelStoreCd;

    /**
     * 대표자아이디
     */
    private String userId;
    /**
     * 사용여부
     */
    private Boolean isUse;
}
