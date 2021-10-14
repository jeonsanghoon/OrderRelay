package com.them.orderrelay.domain.mongo.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.them.orderrelay.domain.mongo.base.MongoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("LogData")
@Getter
@Setter
public class LogDto {
    /**
     * 대행사코드
     */
    private String channelEntpCd = "";
    /**
     * 매장키
     */
    private String storeKey = "";
    /**
     * 로그 생성된 URL
     */
    private String url = "";
    /**
     * 요청 데이터
     */
    private String reqJsonData = "";
    /**
     * 응답 데이터
     */
    private String resJsonData = "";
    /**
     * 메모
     */
    private String memo = "";
    /**
     * 등록일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDtm = LocalDateTime.now().plusHours(MongoConstants.timeZone);
}
