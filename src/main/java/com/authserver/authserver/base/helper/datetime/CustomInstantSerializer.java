package com.authserver.authserver.base.helper.datetime;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.authserver.authserver.base.helper.TimeZoneContext;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomInstantSerializer extends JsonSerializer<Instant> {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {

        if (value == null) {
            gen.writeNull();
            return;
        }

        ZoneId clientZone = TimeZoneContext.getTimeZone();

        String formatted = DateTimeFormatter.ofPattern(FORMAT)
                .withZone(clientZone)   // convert UTC → client timezone
                .format(value);

        gen.writeString(formatted);
    }
}