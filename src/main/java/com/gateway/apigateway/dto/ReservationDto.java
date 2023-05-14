package com.gateway.apigateway.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationDto {
    Long userId;
    Long accId;
    LocalDate start;
    LocalDate end;
    int nog;
}
