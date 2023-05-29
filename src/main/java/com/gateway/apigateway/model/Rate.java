package com.gateway.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    private Long id;
    private Long hostId;
    private Long guestId;
    private int rateValue;
    private LocalDate rateDate;
}
