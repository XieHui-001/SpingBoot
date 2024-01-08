package com.spingboot.demo.spingbootdemo.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 队列可以创建多个，对应不同的任务目标，且需要创建对应的路由进行处理。

    public static String directExchangeName = "directExchange"; // 交换机名称
    public static String queue1Name = "queue1"; // 队列一名称
    public static String queue2Name = "queue2"; // 队列二名称
    public static String routingKey1 = "routingKey1"; // 路由一key
    public static String routingKey2 = "routingKey2"; // 路由二key
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directExchangeName);
    }

    @Bean
    public Queue queue1() {
        return new Queue(queue1Name);
    }

    @Bean
    public Queue queue2() {
        return new Queue(queue2Name);
    }

    @Bean
    public Binding binding1(Queue queue1, DirectExchange directExchange) {
        return BindingBuilder.bind(queue1).to(directExchange).with(routingKey1);
    }

    @Bean
    public Binding binding2(Queue queue2, DirectExchange directExchange) {
        return BindingBuilder.bind(queue2).to(directExchange).with(routingKey2);
    }
}

