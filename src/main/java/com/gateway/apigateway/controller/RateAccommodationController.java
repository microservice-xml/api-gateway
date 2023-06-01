package com.gateway.apigateway.controller;

import com.gateway.apigateway.model.Rate;
import com.gateway.apigateway.model.RateAccommodation;
import com.gateway.apigateway.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rate-accommodation")
@RequiredArgsConstructor
public class RateAccommodationController {

    @Autowired
    RateService rateService;

    @PostMapping("")
    public ResponseEntity rateAccommodation(@RequestBody RateAccommodation rate){
        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.rateAccommodation(rate));
    }
    @PutMapping("/{id}")
    public ResponseEntity changeAccommodationRate(@RequestBody RateAccommodation rate){
        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.changeAccommodationRate(rate));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAccommodationRate(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(rateService.deleteAccommodationRate(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity findAllByAccommodationId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(rateService.findAllByAccommodationId(id));
    }
}
