package com.them.orderrelay.domain.mongo.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.them.orderrelay.domain.mongo.base.MongoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 배달중계 가맹점
 */
@Document("Store")
@Getter
@Setter
public class StoreDto {
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
     * 배달대행사 코드
     */
    private String channelEntpCd;
    /**
     * 배달대행사명
     */
    private String dlvEntpNm;
    /**
     * 배달대행사에서 관리하는 storeCd 매장 구분키
     */
    private String channelStoreCd;
    /**
     * 예치금
     */
    private Double depositAmt;
    /**
     * 대표자아이디
     */
    private String userId;
    /**
     * 사용여부
     */
    private Boolean isUse = true;
    /**
     * 등록자
     */
    private String regId;
    /**
     * 등록일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDtm = LocalDateTime.now().plusHours(MongoConstants.timeZone);
    /**
     * 수정자
     */
    private String modId;
    /**
     * 수정일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modDtm = LocalDateTime.now().plusHours(MongoConstants.timeZone);
    /**
     * 사업자번호
     */
    private String businessNo;
    /**
     * 가맹점 도로명 주소
     */
    private String storeLoadAddr;
    /**
     * 가맹점 구 주소
     */
    private String storeAddr;
    /**
     * 가맹점주 이름
     */
    private String ceoNm;
    /**
     * 가맹점주 전화번호
     */
    private String ceoPhoneNo;
}
