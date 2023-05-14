package com.gateway.apigateway.mapper;

import com.gateway.apigateway.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gateway.apigateway.mapper.ReservationMapper.convertReservationGrpcToReservation;

@Component
@RequiredArgsConstructor
public class AvailabilitySlotMapper {
    public static com.gateway.apigateway.model.AvailabilitySlot convertAvailabilitySlotGrpcToAvailabilitySlot(communication.AvailabilitySlotFull availabilitySlotFull){
        return com.gateway.apigateway.model.AvailabilitySlot.builder()
                .id(availabilitySlotFull.getId())
                .price(availabilitySlotFull.getPrice())
                .reservations(convertReservationGrpcListToReservationList(availabilitySlotFull.getReservationsList()))
                .start(LocalDate.of(availabilitySlotFull.getStartYear(), availabilitySlotFull.getStartMonth(), availabilitySlotFull.getStartDay()))
                .end(LocalDate.of(availabilitySlotFull.getEndYear(), availabilitySlotFull.getEndMonth(), availabilitySlotFull.getEndDay()))
                .build();
    }

    public static List<Reservation> convertReservationGrpcListToReservationList(List<communication.Reservation> reservationList) {
        List<Reservation> retVal = new ArrayList<>();
        for (communication.Reservation res: reservationList) {
            retVal.add(convertReservationGrpcToReservation(res));
        }
        return retVal;
    }
}
