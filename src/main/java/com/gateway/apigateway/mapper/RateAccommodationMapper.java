package com.gateway.apigateway.mapper;

import com.gateway.apigateway.model.Rate;
import com.gateway.apigateway.model.RateAccommodation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class RateAccommodationMapper {
    public static communication.AccommodationRate convertFromMessageToRate(RateAccommodation rate) {
        String rateDate = rate.getRateDate().toString();

        communication.AccommodationRate request;
        request = communication.AccommodationRate.newBuilder()
                //.setId(rate.getId())
                .setAccommodationId(rate.getAccommodationId())
                .setHostId(rate.getHostId())
                .setGuestId(rate.getGuestId())
                .setRateValue(rate.getRateValue())
                .setRateDate(rateDate)
                .build();

        return request;
    }

    public static communication.AccommodationRate convertFromMessageToRateWithId(RateAccommodation rate) {
        String rateDate = rate.getRateDate().toString();

        communication.AccommodationRate request;
        request = communication.AccommodationRate.newBuilder()
                .setId(rate.getId())
                .setAccommodationId(rate.getAccommodationId())
                .setHostId(rate.getHostId())
                .setGuestId(rate.getGuestId())
                .setRateValue(rate.getRateValue())
                .setRateDate(rateDate)
                .build();

        return request;
    }
    public static RateAccommodation convertFromMessageToRateAccommodation(communication.AccommodationRate rate) {
        LocalDate rateDate = LocalDate.parse(rate.getRateDate());

        RateAccommodation convertedRate = new RateAccommodation();
        convertedRate.setId(rate.getId());
        convertedRate.setAccommodationId(rate.getAccommodationId());
        convertedRate.setHostId(rate.getHostId());
        convertedRate.setGuestId(rate.getGuestId());
        convertedRate.setRateValue(rate.getRateValue());
        convertedRate.setRateDate(rateDate);

        return convertedRate;
    }


    public static RateAccommodation convertRateRequestToEntity(communication.AccommodationRate request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate rateDate = LocalDate.parse(request.getRateDate(), formatter);

        return RateAccommodation.builder()
                .accommodationId(request.getAccommodationId())
                .hostId(request.getHostId())
                .guestId(request.getGuestId())
                .rateValue(request.getRateValue())
                .rateDate(rateDate)
                .build();
    }

    public static RateAccommodation convertRateRequestToEntityWithId(communication.AccommodationRate request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate rateDate = LocalDate.parse(request.getRateDate(), formatter);

        return RateAccommodation.builder()
                .id(request.getId())
                .accommodationId(request.getAccommodationId())
                .hostId(request.getHostId())
                .guestId(request.getGuestId())
                .rateValue(request.getRateValue())
                .rateDate(rateDate)
                .build();
    }
}
