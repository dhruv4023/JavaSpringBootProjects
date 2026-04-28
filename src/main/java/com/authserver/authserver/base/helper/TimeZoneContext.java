package com.authserver.authserver.base.helper;

import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimeZoneContext {
    private static final ThreadLocal<ZoneId> userTimeZone = new ThreadLocal<>();

    public static void setTimeZone(String zoneId) {
        userTimeZone.set(ZoneId.of(zoneId));
    }

    public static ZoneId getTimeZone() {
        return userTimeZone.get() != null ? userTimeZone.get() : ZoneOffset.UTC;
    }

    public static void clear() {
        userTimeZone.remove();
    }
}


