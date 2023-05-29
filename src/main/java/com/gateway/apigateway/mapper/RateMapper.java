package com.gateway.apigateway.mapper;

import com.gateway.apigateway.dto.User.UserDto;
import com.gateway.apigateway.model.Rate;
import communication.RegisterUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class RateMapper {

    public static communication.Rate convertFromMessageToRate(Rate rate) {
        String rateDate = rate.getRateDate().toString();

        communication.Rate request;
        request = communication.Rate.newBuilder()
                //.setId(rate.getId())
                .setHostId(rate.getHostId())
                .setGuestId(rate.getGuestId())
                .setRateValue(rate.getRateValue())
                .setRateDate(rateDate)
                .build();

        return request;
    }

    public static communication.Rate convertFromMessageToRateWithId(Rate rate) {
        String rateDate = rate.getRateDate().toString();

        communication.Rate request;
        request = communication.Rate.newBuilder()
                .setId(rate.getId())
                .setHostId(rate.getHostId())
                .setGuestId(rate.getGuestId())
                .setRateValue(rate.getRateValue())
                .setRateDate(rateDate)
                .build();

        return request;
    }

    public static Rate convertRateRequestToEntity(communication.Rate request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate rateDate = LocalDate.parse(request.getRateDate(), formatter);

        return Rate.builder()
                .hostId(request.getHostId())
                .guestId(request.getGuestId())
                .rateValue(request.getRateValue())
                .rateDate(rateDate)
                .build();
    }

    public static Rate convertRateRequestToEntityWithId(communication.Rate request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate rateDate = LocalDate.parse(request.getRateDate(), formatter);

        return Rate.builder()
                .id(request.getId())
                .hostId(request.getHostId())
                .guestId(request.getGuestId())
                .rateValue(request.getRateValue())
                .rateDate(rateDate)
                .build();
    }
}
