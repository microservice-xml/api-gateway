package com.gateway.apigateway.controller;

import com.gateway.apigateway.model.Rate;
import com.gateway.apigateway.service.RateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rate")
@RequiredArgsConstructor
public class RateController {
    @Autowired
    RateService rateService;
    private Logger logger = LoggerFactory.getLogger(RateAccommodationController.class);

    @PostMapping("")
    public ResponseEntity rateHost(@RequestBody Rate rate){
        logger.trace("Request to rate the host with id {} with grade {} was made", rate.getHostId(), rate.getRateValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.rateHost(rate));
    }
    @PutMapping("/{id}")
    public ResponseEntity changeRate(@RequestBody Rate rate){
        logger.trace("Request to edit the rating for host with id {} with new grade {} was made", rate.getHostId(), rate.getRateValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.changeRate(rate));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteRate(@PathVariable Long id){
        logger.trace("Request to delete the host rating with id {} was made", id);
        return ResponseEntity.status(HttpStatus.OK).body(rateService.deleteRate(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity getALlByHostId(@PathVariable Long id){
        logger.trace("Request to find all ratings for host with id {} was made", id);
        return ResponseEntity.status(HttpStatus.OK).body(rateService.getAllByHostId(id));
    }
}
