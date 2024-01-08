package com.spingboot.demo.spingbootdemo.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageReceiver {

    @RabbitListener(queues = "queue1",concurrency = "5-20")
    public void receiveMessageFromQueue1(String message) {
        // 做不同的处理
        log.info("Received message from Queue1: " + message);
    }

    @RabbitListener(queues = "queue2",concurrency = "5-20")
    public void receiveMessageFromQueue2(String message) {
        log.info("Received message from Queue2: " + message);
    }
}

