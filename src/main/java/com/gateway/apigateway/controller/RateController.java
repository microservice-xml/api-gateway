package com.gateway.apigateway.controller;

import com.gateway.apigateway.model.Rate;
import com.gateway.apigateway.service.RateService;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("")
    public ResponseEntity rateHost(@RequestBody Rate rate){
        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.rateHost(rate));
    }
    @PutMapping("/{id}")
    public ResponseEntity changeRate(@RequestBody Rate rate){
        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.changeRate(rate));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteRate(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(rateService.deleteRate(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity getALlByHostId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(rateService.getAllByHostId(id));
    }
}
