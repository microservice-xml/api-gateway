package com.gateway.apigateway.controller;

import com.gateway.apigateway.model.AvailabilitySlot;
import com.gateway.apigateway.service.AvailabilitySlotService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/availability-slot")
public class AvailabilitySlotController {
    private final AvailabilitySlotService availabilitySlotService;
    private Logger logger = LoggerFactory.getLogger(AvailabilitySlotController.class);

    @PostMapping
    public ResponseEntity add(@RequestBody AvailabilitySlot availabilitySlot) {
        logger.trace("Request to add an availability slot for accommodation with id {} and date range from {} to {} was made",
                availabilitySlot.getAccommodationId(), availabilitySlot.getStart(), availabilitySlot.getEnd());
        availabilitySlotService.add(availabilitySlot);
        return ResponseEntity.status(HttpStatus.OK).body("Availability slot created successfully.");
    }

    @PutMapping
    public ResponseEntity edit(@RequestBody AvailabilitySlot availabilitySlot) {
        logger.trace("Request to edit an availability slot for accommodation with id {} and date range from {} to {} was made",
                availabilitySlot.getAccommodationId(), availabilitySlot.getStart(), availabilitySlot.getEnd());
        availabilitySlotService.edit(availabilitySlot);
        return ResponseEntity.status(HttpStatus.OK).body("Availability slot updated successfully.");
    }

    @GetMapping("/accommodation/{accommodationId}")
    public ResponseEntity getAllByAccommodationId(@PathVariable Long accommodationId) {
        logger.trace("Request to find all availability slots for accommodation with id {} was made", accommodationId);
        var availabilitySlots = availabilitySlotService.getAllByAccommodationId(accommodationId);
        return ResponseEntity.status(HttpStatus.OK).body(availabilitySlots);
    }
}