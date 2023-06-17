package com.gateway.apigateway.controller;

import com.gateway.apigateway.dto.AccommodationDto;
import com.gateway.apigateway.dto.AccommodationSearchDto;
import com.gateway.apigateway.model.Accommodation;
import com.gateway.apigateway.model.User;
import com.gateway.apigateway.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<AccommodationDto>> search(@RequestBody AccommodationSearchDto accommodationSearchDto) {
        return ResponseEntity.status(OK).body(accommodationService.search(accommodationSearchDto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Accommodation>> findAllByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(accommodationService.findAllByUser(userId), OK);
    }
    @PostMapping("/add-accommodation")
    public ResponseEntity addAccommodation(@RequestBody Accommodation accommodation){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(accommodationService.addAccommodation(accommodation));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Create accommodation failed.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(accommodationService.findById(id));
    }

    @GetMapping("/recommend/{id}")
    public ResponseEntity recommend(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(accommodationService.recommend(id));
    }

}