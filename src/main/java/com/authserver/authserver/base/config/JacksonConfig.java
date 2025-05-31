package com.authserver.authserver.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        objectMapper.registerModule(javaTimeModule);

        return objectMapper;
    }
}
