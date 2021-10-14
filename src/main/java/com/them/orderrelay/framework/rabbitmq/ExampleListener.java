package com.them.orderrelay.framework.rabbitmq;

import com.them.orderrelay.framework.util.Global;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExampleListener {
    @RabbitListener(queues = "${spring.rabbitmq.queue-name}")
    public void receiveMessage(Object message) {
        Global.getLogInfo().info(log,"RabbitMQ receiveMessage >> " , message);
    }
}
