package com.them.orderrelay.domain.mongo.base.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 리플레시토큰
 */
public class RefreshTokenDto {
    /**
     * 토큰
     */
    private String token;
    /**
     * 리플레시토큰
     */
    private String refreshToken;
    /**
     * 토큰만료 시간 1주일
     */
    private Long tokenValidTimes = 604800000l;
    /**
     * 리플레시토큰 만료시간 2주일
     */
    private Long refreshTokenValidTimes = 604800000l;
}
