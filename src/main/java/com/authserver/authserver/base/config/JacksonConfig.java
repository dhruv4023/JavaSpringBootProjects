package com.authserver.authserver.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.authserver.authserver.base.helper.datetime.CustomInstantDeserializer;
import com.authserver.authserver.base.helper.datetime.CustomInstantSerializer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JavaTimeModule module = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        module.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(formatter));

        module.addDeserializer(Instant.class, new CustomInstantDeserializer());
        module.addSerializer(Instant.class, new CustomInstantSerializer());
        mapper.registerModule(module);
        return mapper;
    }
}
