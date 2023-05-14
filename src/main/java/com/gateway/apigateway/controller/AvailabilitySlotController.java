package com.gateway.apigateway.controller;

import com.gateway.apigateway.model.AvailabilitySlot;
import com.gateway.apigateway.service.AvailabilitySlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/availability-slot")
public class AvailabilitySlotController {
    private final AvailabilitySlotService availabilitySlotService;

    @PostMapping
    public ResponseEntity add(@RequestBody AvailabilitySlot availabilitySlot) {
        availabilitySlotService.add(availabilitySlot);
        return ResponseEntity.status(HttpStatus.OK).body("Availability slot created successfully.");
    }

    @PutMapping
    public ResponseEntity edit(@RequestBody AvailabilitySlot availabilitySlot) {
        availabilitySlotService.edit(availabilitySlot);
        return ResponseEntity.status(HttpStatus.OK).body("Availability slot updated successfully.");
    }

    @GetMapping("/accommodation/{accommodationId}")
    public ResponseEntity getAllByAccommodationId(@PathVariable Long accommodationId) {
        var availabilitySlots = availabilitySlotService.getAllByAccommodationId(accommodationId);
        return ResponseEntity.status(HttpStatus.OK).body(availabilitySlots);
    }
}