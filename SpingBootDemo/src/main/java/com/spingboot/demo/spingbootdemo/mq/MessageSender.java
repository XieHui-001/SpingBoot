package com.spingboot.demo.spingbootdemo.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(String message,  String routingKey) {
        amqpTemplate.convertAndSend(RabbitMQConfig.directExchangeName, routingKey, message);
    }
}
