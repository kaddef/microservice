package com.project.appointment.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${sample.rabbitmq.exchange}")
    String exchange;

    @Value("${sample.rabbitmq.queueName}")
    String queueName;

    @Value("${sample.rabbitmq.routingKey}")
    String routingKey;

    @Bean
    DirectExchange exchange() {return new DirectExchange(exchange);}

    @Bean
    Queue queue() {return new Queue(queueName, false);}

    @Bean
    Binding binding(DirectExchange exchange, Queue Queue) {
        return BindingBuilder.bind(Queue).to(exchange).with(routingKey);
    }

    @Bean
    public MessageConverter messageConverter() {return new Jackson2JsonMessageConverter();}
}
