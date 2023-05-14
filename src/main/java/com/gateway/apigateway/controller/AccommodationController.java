package com.gateway.apigateway.controller;

import com.gateway.apigateway.dto.AccommodationSearchDto;
import com.gateway.apigateway.model.Accommodation;
import com.gateway.apigateway.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/accommodation")
@RequiredArgsConstructor
public class AccommodationController {
    @Autowired
    AccommodationService accommodationService;

    @GetMapping("/all")
    public ResponseEntity<List<Accommodation>> findAll() {
        return new ResponseEntity<>(accommodationService.findAll(), OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Accommodation>> search(@RequestBody AccommodationSearchDto accommodationSearchDto) {
        return ResponseEntity.status(OK).body(accommodationService.search(accommodationSearchDto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Accommodation>> findAllByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(accommodationService.findAllByUser(userId), OK);
    }
}