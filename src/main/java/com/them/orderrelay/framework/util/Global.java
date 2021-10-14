package com.them.orderrelay.framework.util;

import com.them.orderrelay.framework.util.mail.MailInfo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Global {
    @Getter
    private static LogInfo logInfo;

    @Getter
    private static DateInfo dateInfo;

    @Getter
    private static DataInfo dataInfo;

    @Getter
    private static ValidationInfo validationInfo;

    @Getter
    private static MessageInfo messageInfo;

    @Getter
    private static SecurityInfo securityInfo;

    @Getter
    private static NumberInfo numberInfo;

    @Getter
    private static MailInfo mailInfo;

    @Autowired
    private Global(
                   DataInfo dataInfo,
                   DateInfo dateInfo,
                   LogInfo logInfo,
                   ValidationInfo validationInfo,
                   MessageInfo messageInfo,
                   SecurityInfo securityInfo,
                   NumberInfo numberInfo,
                   MailInfo mailInfo
                ) {
        this.dataInfo = dataInfo;
        this.dateInfo = dateInfo;
        this.validationInfo = validationInfo;
        this.logInfo = logInfo;
        this.messageInfo = messageInfo;
        this.securityInfo = securityInfo;
        this.numberInfo = numberInfo;
        this.mailInfo = mailInfo;
    }
}
