package com.authserver.authserver.communication.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.authserver.authserver.base.exception.ValidationException;
import com.authserver.authserver.communication.exceptions.EmailSendingException;
import com.authserver.authserver.communication.models.EmailCredentials;
import com.authserver.authserver.communication.repository.EmailCredentialsRepository;

import java.util.Objects;
import java.util.Properties;

@Component
@Slf4j
public class EmailUtil {

    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private int mailPort;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttls;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;
    @Value("${spring.mail.username}")
    private String emailName;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.default-encoding}")
    private String defaultEncoding;

    @Value("${spring.mail.debug}")
    private String sendMail;

    @Autowired
    private EmailCredentialsRepository emailCredentialsRepository;

    public Boolean sendEmail(String to, String subject, String body,
            Long userId,
            byte[] attachmentBytes, String attachmentFileName) {
        Objects.requireNonNull(to, "To address cannot be null");
        Objects.requireNonNull(subject, "Subject cannot be null");
        Objects.requireNonNull(body, "Body cannot be null");

        if (Boolean.valueOf(sendMail)) {
            log.info("Sending email - to: {}, subject: {}, body: {}, studioId: {}, attachmentFileName: {}", to, subject,
                    body, null, attachmentFileName);
        } else {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(mailHost);
            mailSender.setPort(mailPort);
            mailSender.setProtocol("smtp");
            mailSender.setDefaultEncoding(defaultEncoding);

            if (userId != null) {
                EmailCredentials studio = emailCredentialsRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException("Email Credentials not found"));
                if (!StringUtils.hasText(studio.getPasscode())) {
                    throw new ValidationException("Studio email passcode is missing");
                }
                mailSender.setUsername(studio.getUser().getEmail());
                mailSender.setPassword(studio.getPasscode());
            } else {
                mailSender.setUsername(emailName);
                mailSender.setPassword(password);
            }

            // Mail properties
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", auth);
            props.put("mail.smtp.starttls.enable", starttls);
            props.put("mail.debug", "false");

            // Always use MimeMessage for consistency
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper;
            try {
                helper = new MimeMessageHelper(mimeMessage, attachmentBytes != null);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(body, false); // false = plain text, true = HTML

                if (attachmentBytes != null && attachmentFileName != null) {
                    helper.addAttachment(
                            attachmentFileName,
                            new ByteArrayDataSource(attachmentBytes, "application/octet-stream"));
                }
            } catch (MessagingException e) {
                throw new EmailSendingException("Failed to send email", e);
            }

            mailSender.send(mimeMessage);
        }
        return true;
    }
}
