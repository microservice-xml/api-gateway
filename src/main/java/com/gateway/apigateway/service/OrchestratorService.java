package com.gateway.apigateway.service;

import communication.AccommodationServiceGrpc;
import communication.UserIdRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrchestratorService {

    @Value("${accommodation-api.grpc.address}")
    private String accommodationApiGrpcAddress;

    @Value("${user-api.grpc.address}")
    private String userApiGrpcAddress;

    @Value("${reservation-api.grpc.address}")
    private String reservationApiGrpcAddress;

    public String deleteUser(Long userId) {
        var accommodationIds = checkAccommodations(userId);
        if (accommodationIds.size() == 0) {
            return finishDelete(userId);
        }
        communication.BooleanResponse response = checkReservations(accommodationIds);
        if (response.getAvailable()) {
            return finishDelete(userId);
        }

        return "User cannot be deleted due to future reservations in one of the accommodations.";
    }

    private ManagedChannel openChannel(String address, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(address, port)
                .usePlaintext()
                .build();

        return channel;
    }

    private List<Long> checkAccommodations(Long userId) {
        ManagedChannel channel = openChannel(accommodationApiGrpcAddress, 9094);
        communication.UserAccommodationServiceGrpc.UserAccommodationServiceBlockingStub blockingStub = communication.UserAccommodationServiceGrpc.newBlockingStub(channel);
        communication.AccommodationResponse response = blockingStub.checkForDelete(UserIdRequest.newBuilder().setId(userId).build());
        channel.shutdown();
        return response.getAccommodationIdsList();
    }

    private communication.BooleanResponse checkReservations(List<Long> accommodationIds) {
        ManagedChannel channel = openChannel(reservationApiGrpcAddress, 9095);
        AccommodationServiceGrpc.AccommodationServiceBlockingStub blockingStub = AccommodationServiceGrpc.newBlockingStub(channel);
        return blockingStub.checkForDelete(createMessage(accommodationIds));
    }

    private communication.CheckDeleteRequest createMessage(List<Long> accommodationIds) {
        return communication.CheckDeleteRequest
                .newBuilder()
                .addAllAccommodationIds(accommodationIds)
                .setStartYear(LocalDate.now().getYear())
                .setStartMonth(LocalDate.now().getMonthValue())
                .setStartDay(LocalDate.now().getDayOfMonth())
                .build();
    }

    private String finishDelete(Long userId){
        ManagedChannel channel = openChannel(userApiGrpcAddress, 9093);
        communication.userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = communication.userDetailsServiceGrpc.newBlockingStub(channel);
        communication.MessageResponse message = blockingStub.delete(communication.UserIdRequest.newBuilder().setId(userId).build());
        channel.shutdown();
        return message.getMessage();
    }
}
