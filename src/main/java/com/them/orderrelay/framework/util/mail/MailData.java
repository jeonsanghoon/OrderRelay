package com.them.orderrelay.framework.util.mail;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
public class MailData {
    /*
     * Html 템플릿 Default : "/templeate/mail-template.html"
     */
    private String templeateName;
    /*
     * 이메일 제목
     */
    private String title;
    /*
     * 보내는 사람 메일주소 Default
     */
    @Builder.Default
    private String fromMail = "service@themcompany.kr";
    /*
     * 보내는 사람 표기명
     */
    @Builder.Default
    private String fromMailName = "프렌즈플러스";
    /*
     * 받는사람 메일주소
     */
    private String toMail;
    /*
     * 받는사람 표기명
     */
    @Builder.Default
    private String toMailName="";
    /*
     * 참조자 주소
     */
    private String ccMail;
    /*
     * 참조자 표기명
     */
    @Builder.Default
    private String ccMailName="";
    private String message;
    @Builder.Default
    private MessageType messageType = MessageType.info;

}

