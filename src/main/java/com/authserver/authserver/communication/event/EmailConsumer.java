package com.authserver.authserver.communication.event;

import com.authserver.authserver.communication.util.EmailUtil;
import com.authserver.authserver.rabbitmq.RabbitConfig;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailConsumer {

    private final EmailUtil emailUtil;

    EmailConsumer(EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
    }

    @RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
    public void handleUserCreated(ConsumerUserCreatedEvent event) {
        System.out.println("Sending email to: " + event.getToEmail());

        try {
            emailUtil.sendEmail(event.getToEmail(), event.getSubject(), event.getBody(), event.getSenderId(),
                    event.getAttachmentBytes(), event.getAttachmentFileName());
        } catch (Exception e) {
            log.error("Email sending failed", e);
        }
    }
}