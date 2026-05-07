package com.authserver.authserver.base.service;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneOffset;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authserver.authserver.base.helper.TimeZoneContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TimeZoneFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String timeZoneHeader = request.getHeader("Time-Zone");

        if (timeZoneHeader != null && !timeZoneHeader.isEmpty()) {
            try {
                TimeZoneContext.setTimeZone(timeZoneHeader);
            } catch (DateTimeException e) {
                TimeZoneContext.setTimeZone(ZoneOffset.UTC.getId());
            }
        } else {
            TimeZoneContext.setTimeZone(ZoneOffset.UTC.getId());
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TimeZoneContext.clear();
        }
    }
}
