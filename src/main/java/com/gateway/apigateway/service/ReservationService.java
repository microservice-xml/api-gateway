package com.gateway.apigateway.service;

import com.gateway.apigateway.dto.ReservationDto;
import com.gateway.apigateway.model.Reservation;
import communication.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gateway.apigateway.mapper.ReservationMapper.convertReservationGrpcToReservation;
import static com.gateway.apigateway.mapper.ReservationMapper.convertReservationToReservationGrpc;
import static com.gateway.apigateway.mapper.ReservationStatusMapper.convertReservationStatusToReservationStatusGrpc;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final AvailabilitySlotService availabilitySlotService;
    private Logger logger = LoggerFactory.getLogger(ReservationService.class);
    @Value("${reservation-api.grpc.address}")
    private String reservationApiGrpcAddress;

    private ReservationServiceGrpc.ReservationServiceBlockingStub getStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        return ReservationServiceGrpc.newBlockingStub(channel);
    }

    public String create(ReservationDto reservationDto) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = ReservationServiceGrpc.newBlockingStub(channel);
        logger.info("Request for create new reservation request by guest [ID: %d]",reservationDto.getUserId());
        Reservation reservation = findAvailabilitySlotForReservation(reservationDto);
        MessageResponse response = blockingStub.createRequest(convertReservationToReservationGrpc(reservation));
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return response.getMessage();
    }

    private Reservation findAvailabilitySlotForReservation(ReservationDto reservationDto){
        List<com.gateway.apigateway.model.AvailabilitySlot> accommodations = availabilitySlotService.getAllByAccommodationId(reservationDto.getAccId());
        for(com.gateway.apigateway.model.AvailabilitySlot ac : accommodations){
            if(isInRange(reservationDto.getStart(),reservationDto.getEnd(),ac.getStart(),ac.getEnd())){
                return Reservation.builder()
                        .slotId(ac.getId())
                        .end(reservationDto.getEnd())
                        .start(reservationDto.getStart())
                        .numberOfGuests(reservationDto.getNog())
                        .userId(reservationDto.getUserId())
                        .hostId(reservationDto.getHostId())
                        .build();
            }
        }
        return null;
    }

    public boolean isInRange(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return startDate1.compareTo(startDate2) >= 0 && endDate1.compareTo(endDate2) <= 0;
    }

    public Reservation findById(String id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = ReservationServiceGrpc.newBlockingStub(channel);
        var retVal = blockingStub.findById(Id.newBuilder().setId(id).build());
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return convertReservationGrpcToReservation(retVal);
    }

    public String createAuto(ReservationDto reservationDto) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = ReservationServiceGrpc.newBlockingStub(channel);
        logger.info("Request for create new reservation by guest. [ID: %d]",reservationDto.getUserId());
        Reservation reservation = findAvailabilitySlotForReservation(reservationDto);
        MessageResponse response = blockingStub.acceptReservationAuto(convertReservationToReservationGrpc(reservation));
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return response.getMessage();
    }

    public String cancel(String id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();

        logger.info("Request for remove reservation. [ID: %s]",id);
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = ReservationServiceGrpc.newBlockingStub(channel);
        MessageResponse response = blockingStub.cancel(Id.newBuilder().setId(id).build());
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return response.getMessage();
    }

    public String reject(String id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = ReservationServiceGrpc.newBlockingStub(channel);
        logger.info("Request for reject reservation request. [ID: %s]",id);
        MessageResponse response = blockingStub.rejectRequest(Id.newBuilder().setId(id).build());
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return response.getMessage();
    }

    public String accept(String id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = ReservationServiceGrpc.newBlockingStub(channel);
        logger.info("Request for accpet reservation request. [ID: %s]",id);
        MessageResponse response = blockingStub.acceptReservationManual(Id.newBuilder().setId(id).build());
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return response.getMessage();
    }

    public List<Reservation> findAll() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = ReservationServiceGrpc.newBlockingStub(channel);
        ListReservation reservations = blockingStub.findAll(EmptyMessage.newBuilder().build());
        List<Reservation> retVal = new ArrayList<>();
        for(communication.Reservation res : reservations.getReservationsList()){
            retVal.add(convertReservationGrpcToReservation(res));
        }
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return retVal;
    }

    public List<Reservation> findAllByStatus(com.gateway.apigateway.model.ReservationStatus status) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = ReservationServiceGrpc.newBlockingStub(channel);
        ListReservation reservations = blockingStub.findAllByStatus(Status.newBuilder().setStatus(convertReservationStatusToReservationStatusGrpc(status)).build());
        List<Reservation> retVal = new ArrayList<>();
        for(communication.Reservation res : reservations.getReservationsList()){
            retVal.add(convertReservationGrpcToReservation(res));
        }
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return retVal;
    }

    public List<Reservation> findAllByUserId(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = ReservationServiceGrpc.newBlockingStub(channel);
        ListReservation reservations = blockingStub.findAllByUserId(LongId.newBuilder().setId(id).build());
        List<Reservation> retVal = new ArrayList<>();
        for(communication.Reservation res : reservations.getReservationsList()){
            retVal.add(convertReservationGrpcToReservation(res));
        }
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return retVal;
    }

    public List<Reservation> findAllByHostId(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = ReservationServiceGrpc.newBlockingStub(channel);
        ListReservation reservations = blockingStub.findAllByHostId(LongId.newBuilder().setId(id).build());
        List<Reservation> retVal = new ArrayList<>();
        for(communication.Reservation res : reservations.getReservationsList()){
            retVal.add(convertReservationGrpcToReservation(res));
        }
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return retVal;
    }

    public List<Reservation> findByAccomodationId(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = ReservationServiceGrpc.newBlockingStub(channel);
        ListReservation reservations = blockingStub.findByAccomodationId(LongId.newBuilder().setId(id).build());
        List<Reservation> retVal = new ArrayList<>();
        for(communication.Reservation res : reservations.getReservationsList()){
            retVal.add(convertReservationGrpcToReservation(res));
        }
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return retVal;
    }
}
