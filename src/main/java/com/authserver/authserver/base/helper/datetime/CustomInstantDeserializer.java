package com.authserver.authserver.base.helper.datetime;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.authserver.authserver.base.helper.TimeZoneContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomInstantDeserializer extends JsonDeserializer<Instant> {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        String value = p.getText();
        if (value == null || value.isEmpty()) return null;

        ZoneId clientZone = TimeZoneContext.getTimeZone();

        LocalDateTime localDateTime = LocalDateTime.parse(
                value,
                DateTimeFormatter.ofPattern(FORMAT)
        );

        return localDateTime
                .atZone(clientZone)              // interpret as client timezone
                .withZoneSameInstant(ZoneId.of("UTC")) // convert to UTC
                .toInstant();
    }
}