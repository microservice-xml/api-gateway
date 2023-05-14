package com.gateway.apigateway.service;

import com.gateway.apigateway.dto.ReservationDto;
import com.gateway.apigateway.model.Reservation;
import communication.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
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
    private ReservationServiceGrpc.ReservationServiceBlockingStub getStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9095)
                .usePlaintext()
                .build();
        return ReservationServiceGrpc.newBlockingStub(channel);
    }

    public String create(ReservationDto reservationDto) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();

        Reservation reservation = findAvailabilitySlotForReservation(reservationDto);
        MessageResponse response = blockingStub.createRequest(convertReservationToReservationGrpc(reservation));
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
                        .build();
            }
        }
        return null;
    }

    public boolean isInRange(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return startDate1.compareTo(startDate2) >= 0 && endDate1.compareTo(endDate2) <= 0;
    }

    public Reservation findById(String id) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        return convertReservationGrpcToReservation(blockingStub.findById(Id.newBuilder().setId(id).build()));
    }

    public String createAuto(ReservationDto reservationDto) {
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();
        Reservation reservation = findAvailabilitySlotForReservation(reservationDto);
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
