package com.them.orderrelay.domain.mongo.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TokenDto {
    /**
     * 사용자ID
     */
    private String userId;
    /**
     * 사용자명
     */
    private String userNm;
    /**
     * 패스워드 (SHA256)
     */
    private String userPw;
    /**
     * 토큰
     */
    private String token;
    /**
     * 계정사용유무
     */
    private Boolean isUse = true;
    /**
     * 리플레시토큰
     */
    private String refreshToken;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tokenExpireDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refreshTokenExpireDate;
}
