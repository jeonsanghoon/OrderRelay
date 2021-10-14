package com.them.orderrelay.domain.mongo.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.them.orderrelay.domain.mongo.base.MongoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Getter
@Setter
public class LogDtoReq {
    /**
     * 몽고 ID
     */
    @MongoId
    private String id;
    /**
     * 매장키
     */
    private String storeKey;
    /**
     * 로그 생성된  URL
     */
    private String url;
    /**
     * 검색 문자 - memo 검색
     */
    private String searchText;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime frRegDtm = LocalDateTime.now().plusHours(MongoConstants.timeZone);
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime toRegDtm = LocalDateTime.now().plusHours(MongoConstants.timeZone);
}
