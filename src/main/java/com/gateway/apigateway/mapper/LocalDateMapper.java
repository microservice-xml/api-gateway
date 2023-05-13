package com.gateway.apigateway.mapper;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class LocalDateMapper {
    public static Timestamp convertLocalDateToGoogleTimestamp(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    public static LocalDate convertGoogleTimestampToLocalDate(Timestamp timestamp) {
        return Instant
                .ofEpochSecond(timestamp.getSeconds() , timestamp.getNanos())
                .atZone(ZoneOffset.UTC)
                .toLocalDate();
    }
}

