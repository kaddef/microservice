package com.project.appointment.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Value("${sample.rabbitmq.routingKey}")
    String routingKey;
    @Value("${sample.rabbitmq.queueName}")
    String queueName;

    private final AmqpTemplate rabbitTemplate;
    private final DirectExchange exchange;

    public MessageService(AmqpTemplate rabbitTemplate, DirectExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void sendMessageToQueue(Object message) {
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, message);
    }
}
