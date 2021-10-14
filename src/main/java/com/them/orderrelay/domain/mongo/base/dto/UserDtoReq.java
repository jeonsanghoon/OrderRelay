package com.them.orderrelay.domain.mongo.base.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
public class UserDtoReq {
    /**
     * mongo id
     */
    @MongoId
    private String id;
    /**
     * 사용자ID
     */
    private String userId;
    /**
     * 패스워드 (SHA256)
     */
    private String userPw;

    /**
     * 리플래쉬 토큰
     */
    private String refreshToken;

    /**
     * 사용여부
     */
    private Boolean isUse = true;

    /**
     * 토큰만료 시간
     */
    private Long tokenValidTimes = 604800000l;
    /**
     * 리플레시토큰 만료시간
     */
    private Long refreshTokenValidTimes = 604800000l;
}
