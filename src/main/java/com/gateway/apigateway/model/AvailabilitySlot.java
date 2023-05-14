package com.gateway.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilitySlot {
    private String id;
    private LocalDate start;
    private  LocalDate end;
    private double price;
    private Long accommodationId;
    private List<Reservation> reservations;

}
