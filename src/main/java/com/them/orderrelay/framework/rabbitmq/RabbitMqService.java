package com.them.orderrelay.framework.rabbitmq;

import com.them.orderrelay.framework.exception.UserException;
import com.them.orderrelay.framework.util.Global;
import com.them.orderrelay.framework.util.mail.MailData;
import com.them.orderrelay.framework.util.mail.MessageType;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {

    @Value("${spring.rabbitmq.topic-exchange-name}")
    private String topicExchange;

    @Value("${spring.rabbitmq.queue-name}")
    private String queueName;

    @Value("${service.toMails}")
    private String toMails;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public <T> void send(Logger log, String routeKey, T val) throws UserException
    {
        try {
            Global.getLogInfo().info(log, String.format("RabbitMq routeKey(%s)", routeKey), Global.getDataInfo().convertToString(val));
            rabbitTemplate.convertAndSend(topicExchange,  routeKey, val);

        }catch(Exception e)
        {
            Global.getMailInfo().sendMail(
                    MailData.builder().toMail(toMails)
                            .messageType(MessageType.error)
                            .title("[프렌즈플러스] MQ 서버 알림이 있습니다.")
                            .message("메세지서버에 장애가 발생하였습니다." + e.getMessage())
                            .build()
            );
            throw new UserException(e);
        }
    }
}
