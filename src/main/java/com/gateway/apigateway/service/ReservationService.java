package com.gateway.apigateway.service;

import com.gateway.apigateway.model.Reservation;
import communication.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.gateway.apigateway.mapper.ReservationMapper.convertReservationGrpcToReservation;
import static com.gateway.apigateway.mapper.ReservationMapper.convertReservationToReservationGrpc;
import static com.gateway.apigateway.mapper.ReservationStatusMapper.convertReservationStatusToReservationStatusGrpc;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private ReservationServiceGrpc.ReservationServiceBlockingStub getStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9095)
                .usePlaintext()
                .build();
        return ReservationServiceGrpc.newBlockingStub(channel);
    }

    public String create(Reservation reservation) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        MessageResponse response = blockingStub.createRequest(convertReservationToReservationGrpc(reservation));
        return response.getMessage();
    }

    public Reservation findById(String id) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        return convertReservationGrpcToReservation(blockingStub.findById(Id.newBuilder().setId(id).build()));
    }

    public String createAuto(Reservation reservation) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        MessageResponse response = blockingStub.acceptReservationAuto(convertReservationToReservationGrpc(reservation));
        return response.getMessage();
    }

    public String cancel(String id) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        MessageResponse response = blockingStub.cancel(Id.newBuilder().setId(id).build());
        return response.getMessage();
    }

    public String reject(String id) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        MessageResponse response = blockingStub.rejectRequest(Id.newBuilder().setId(id).build());
        return response.getMessage();
    }

    public String accept(String id) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        MessageResponse response = blockingStub.acceptReservationManual(Id.newBuilder().setId(id).build());
        return response.getMessage();
    }

    public List<Reservation> findAll() {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        ListReservation reservations = blockingStub.findAll(EmptyMessage.newBuilder().build());
        List<Reservation> retVal = new ArrayList<>();
        for(communication.Reservation res : reservations.getReservationsList()){
            retVal.add(convertReservationGrpcToReservation(res));
        }
        return retVal;
    }

    public List<Reservation> findAllByStatus(com.gateway.apigateway.model.ReservationStatus status) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        ListReservation reservations = blockingStub.findAllByStatus(Status.newBuilder().setStatus(convertReservationStatusToReservationStatusGrpc(status)).build());
        List<Reservation> retVal = new ArrayList<>();
        for(communication.Reservation res : reservations.getReservationsList()){
            retVal.add(convertReservationGrpcToReservation(res));
        }
        return retVal;
    }

    public List<Reservation> findAllByUserId(Long id) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        ListReservation reservations = blockingStub.findAllByUserId(LongId.newBuilder().setId(id).build());
        List<Reservation> retVal = new ArrayList<>();
        for(communication.Reservation res : reservations.getReservationsList()){
            retVal.add(convertReservationGrpcToReservation(res));
        }
        return retVal;
    }

    public List<Reservation> findByAccomodationId(Long id) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        ListReservation reservations = blockingStub.findByAccomodationId(LongId.newBuilder().setId(id).build());
        List<Reservation> retVal = new ArrayList<>();
        for(communication.Reservation res : reservations.getReservationsList()){
            retVal.add(convertReservationGrpcToReservation(res));
        }
        return retVal;
    }
}
