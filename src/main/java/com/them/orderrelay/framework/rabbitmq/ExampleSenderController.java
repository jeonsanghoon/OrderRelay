package com.them.orderrelay.framework.rabbitmq;

import com.them.orderrelay.framework.util.Global;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = { "MQ" }, value = "MQ")
@RequestMapping("/mqtt")
@RestController
@Slf4j
public class ExampleSenderController {

    @Value("${spring.rabbitmq.topic-exchange-name}")
    private String topicExchange;

    @Value("${spring.rabbitmq.queue-name}")
    private String queueName;

    private final RabbitTemplate rabbitTemplate;
    public ExampleSenderController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @ApiOperation(value = "메세지보내기", notes = "메세지 보내기 입니다.")
    @PostMapping("/sender")
    public String sender() {
        log.debug("Sending message...");
        Global.getLogInfo().info(log,"/mqtt/sender >> ",  "Sending message...");
        rabbitTemplate.convertAndSend(topicExchange, queueName + ".01", "보내는 메세지");
        return "보내기 완료";
    }
}
