package com.gateway.apigateway.controller;


import com.gateway.apigateway.dto.ReservationDto;
import com.gateway.apigateway.model.Reservation;
import com.gateway.apigateway.model.ReservationStatus;
import com.gateway.apigateway.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @PostMapping
    public ResponseEntity createRequest(@RequestBody ReservationDto reservationDto) {
        logger.trace("Request to create a reservation for accommodation with id {} and date range from {} to {} was made",
                reservationDto.getAccId(), reservationDto.getStart(), reservationDto.getEnd());
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(reservationDto));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please try again later...");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable String id) {
        logger.trace("Request to find the reservation with id {} was made", id);
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.findById(id));
    }


    @GetMapping()
    public ResponseEntity findAll() {
        logger.trace("Request to find all reservations was made");
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.findAll());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity findAllByStatus(@PathVariable ReservationStatus status) {
        logger.trace("Request to find all reservations with status {} was made", status);
        List<Reservation> reservations = reservationService.findAllByStatus(status);
        if (reservations.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("List is empty.");
        else
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity findByUsedId(@PathVariable Long id) {
        logger.trace("Request to find all reservations with user id {} was made", id);
        List<Reservation> reservations = reservationService.findAllByUserId(id);
        if (reservations.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("List is empty.");
        else
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }

    @GetMapping("/hostId/{id}")
    public ResponseEntity findAllByHostId(@PathVariable Long id) {
        logger.trace("Request to find all reservations with host id {} was made", id);
        List<Reservation> reservations = reservationService.findAllByHostId(id);
        if (reservations.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("List is empty.");
        else
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }

    @GetMapping("/accommodation/{id}")
    public ResponseEntity findByAccomodationId(@PathVariable Long id) {
        logger.trace("Request to find all reservations with accommodation id {} was made", id);
        List<Reservation> reservations = reservationService.findByAccomodationId(id);
        if (reservations.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("List is empty.");
        else
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity acceptReservationManual(@PathVariable String id) {
        logger.trace("Request to manually accept the reservation with id {} was made", id);
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.accept(id));
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity rejectRequest(@PathVariable String id) {
        logger.trace("Request to reject the reservation with id {} was made", id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reservationService.reject(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Try again later...");
        }
    }

    @PostMapping("/auto")
    public ResponseEntity acceptReservationAuto(@RequestBody ReservationDto reservationDto) {
        logger.trace("Request to automatically accept the reservation for accommodation with id {} and date range from {} to {} was made",
                reservationDto.getAccId(), reservationDto.getStart(), reservationDto.getEnd());
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createAuto(reservationDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity cancel(@PathVariable String id) {
        logger.trace("Request to cancel the reservation with id {} was made", id);
        try {

            return ResponseEntity.status(HttpStatus.OK).body(reservationService.cancel(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
