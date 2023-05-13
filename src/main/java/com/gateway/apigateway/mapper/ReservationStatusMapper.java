package com.gateway.apigateway.mapper;

import communication.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationStatusMapper {
    public static ReservationStatus convertReservationStatusToReservationStatusGrpc(com.gateway.apigateway.model.ReservationStatus status){
        if(status.equals(com.gateway.apigateway.model.ReservationStatus.ACCEPTED))
            return ReservationStatus.ACCEPTED;
        else if(status.equals(com.gateway.apigateway.model.ReservationStatus.PENDING))
            return ReservationStatus.PENDING;
        else
            return ReservationStatus.DECLINED;

    }

    public static com.gateway.apigateway.model.ReservationStatus convertReservationStatusGrpcToReservationStatus(ReservationStatus status){
        if(status.equals(ReservationStatus.ACCEPTED))
            return com.gateway.apigateway.model.ReservationStatus.ACCEPTED;
        else if(status.equals(ReservationStatus.PENDING))
            return com.gateway.apigateway.model.ReservationStatus.PENDING;
        else
            return com.gateway.apigateway.model.ReservationStatus.DECLINED;

    }
}
