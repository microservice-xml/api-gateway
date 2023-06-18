package com.gateway.apigateway.controller;

import com.gateway.apigateway.model.Rate;
import com.gateway.apigateway.model.RateAccommodation;
import com.gateway.apigateway.service.RateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(RateAccommodationController.class);

    @PostMapping("")
    public ResponseEntity rateAccommodation(@RequestBody RateAccommodation rate){
        logger.trace("Request to rate the accommodation with id {} with grade {} was made", rate.getAccommodationId(), rate.getRateValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.rateAccommodation(rate));
    }
    @PutMapping("/{id}")
    public ResponseEntity changeAccommodationRate(@RequestBody RateAccommodation rate){
        logger.trace("Request to edit the rating for accommodation with id {} with new grade {} was made", rate.getAccommodationId(), rate.getRateValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.changeAccommodationRate(rate));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAccommodationRate(@PathVariable Long id){
        logger.trace("Request to delete the accommodation rating with id {} was made", id);
        return ResponseEntity.status(HttpStatus.OK).body(rateService.deleteAccommodationRate(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity findAllByAccommodationId(@PathVariable Long id){
        logger.trace("Request to find all ratings for accommodation with id {} was made", id);
        return ResponseEntity.status(HttpStatus.OK).body(rateService.findAllByAccommodationId(id));
    }
}
