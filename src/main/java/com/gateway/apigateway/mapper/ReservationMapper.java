package com.gateway.apigateway.mapper;

import com.gateway.apigateway.model.User;
import communication.RegisterUser;
import communication.Reservation;
import communication.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.gateway.apigateway.mapper.LocalDateMapper.convertGoogleTimestampToLocalDate;
import static com.gateway.apigateway.mapper.LocalDateMapper.convertLocalDateToGoogleTimestamp;
import static com.gateway.apigateway.mapper.ReservationStatusMapper.convertReservationStatusGrpcToReservationStatus;

@Component
@RequiredArgsConstructor
public class ReservationMapper {
    public static Reservation convertReservationToReservationGrpc(com.gateway.apigateway.model.Reservation reservation){
        return Reservation.newBuilder()
                .setUserId(reservation.getUserId())
                .setSlotId(reservation.getSlotId())
                .setNumberOfGuests(reservation.getNumberOfGuests())
                .setStart(convertLocalDateToGoogleTimestamp(reservation.getStart()))
                .setEnd(convertLocalDateToGoogleTimestamp(reservation.getEnd()))
                .setHostId(reservation.getHostId())
                .build();
    }

    public static com.gateway.apigateway.model.Reservation convertReservationGrpcToReservation(Reservation reservation){
        return com.gateway.apigateway.model.Reservation.builder()
                .id(reservation.getId())
                .start(convertGoogleTimestampToLocalDate(reservation.getStart()))
                .end(convertGoogleTimestampToLocalDate(reservation.getEnd()))
                .userId(reservation.getUserId())
                .slotId(reservation.getSlotId())
                .numberOfGuests(reservation.getNumberOfGuests())
                .status(convertReservationStatusGrpcToReservationStatus(reservation.getStatus()))
                .hostId(reservation.getHostId())
                .build();
    }
}

