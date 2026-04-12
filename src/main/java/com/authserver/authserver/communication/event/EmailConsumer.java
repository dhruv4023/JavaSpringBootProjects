package com.authserver.authserver.communication.event;

import com.authserver.authserver.communication.util.EmailUtil;
import com.authserver.authserver.rabbitmq.RabbitConfig;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final EmailUtil emailUtil;

    EmailConsumer(EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
    }

    @RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
    public void handleUserCreated(ConsumerUserCreatedEvent event) {
        System.out.println("Sending email to: " + event.getToEmail());

        emailUtil.sendEmail(event.getToEmail(), event.getSubject(), event.getBody(), event.getUserId(),
                event.getAttachmentBytes(), event.getAttachmentFileName());
    }
}