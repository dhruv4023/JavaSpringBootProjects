package com.authserver.authserver.user.events;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.authserver.authserver.rabbitmq.RabbitConfig;

@Service
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserCreated(ProducerEmailEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.USER_CREATED_EVENT,
                event);

        System.out.println("Event " + RabbitConfig.USER_CREATED_EVENT + " sent to RabbitMQ: " + event.getToEmail());
    }

    public void publishForgotPassword(ProducerEmailEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.FORGOT_PASSWORD_EVENT,
                event);

        System.out
                .println("Event " + RabbitConfig.FORGOT_PASSWORD_EVENT + " sent to RabbitMQ: " + event.getToEmail());
    }
}
