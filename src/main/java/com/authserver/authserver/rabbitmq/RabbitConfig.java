package com.authserver.authserver.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // =========================
    // 🔹 SHARED (Producer + Consumer)
    // =========================

    public static final String EXCHANGE = "user.exchange";

    public static final String USER_CREATED_EVENT = "user.created";
    public static final String FORGOT_PASSWORD_EVENT = "user.forgot.password";


    // =========================
    // 🔹 CONSUMER ONLY
    // =========================

    public static final String EMAIL_QUEUE = "email.queue";


    // =========================
    // 🔹 INFRASTRUCTURE (Defined ONCE)
    // Only one service should define these in microservices setup
    // =========================

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true); // durable
    }

    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(userExchange())
                .with(USER_CREATED_EVENT);
    }

    @Bean
    public Binding forgotPasswordBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(userExchange())
                .with(FORGOT_PASSWORD_EVENT);
    }


    // =========================
    // 🔹 SHARED (Producer + Consumer)
    // =========================

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
