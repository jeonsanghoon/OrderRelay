package com.them.orderrelay.domain.mongo.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.them.orderrelay.domain.mongo.base.MongoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("User")
@Getter
@Setter
public class UserDto {
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
     * 리플레시토큰
     */
    private String refreshToken;
    /**
     * 등록자
     */

    private String regId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    /**
     * 등록일시
     */
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
}
