package com.gateway.apigateway.service;

import com.gateway.apigateway.mapper.AccommodationMapper;
import com.gateway.apigateway.mapper.UserMapper;
import com.gateway.apigateway.model.Accommodation;
import com.gateway.apigateway.dto.AccommodationSearchDto;
import communication.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.gateway.apigateway.mapper.AccommodationMapper.convertAccommodationGrpcToAccommodation;
import static com.gateway.apigateway.mapper.ReservationMapper.convertReservationToReservationGrpc;
import static com.gateway.apigateway.mapper.UserMapper.convertUserGrpcToUser;

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private AccommodationServiceGrpc.AccommodationServiceBlockingStub getStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094)
                .usePlaintext()
                .build();
        return AccommodationServiceGrpc.newBlockingStub(channel);
    }

    public List<Accommodation> search(AccommodationSearchDto accommodationSearchDto) {
        AccommodationServiceGrpc.AccommodationServiceBlockingStub blockingStub = getStub();
        SearchAccommodationDto request = SearchAccommodationDto.newBuilder()
                .setLocation(accommodationSearchDto.getLocation())
                .setGuestCount(accommodationSearchDto.getGuestCount())
                .setStartYear(accommodationSearchDto.getStart().getYear())
                .setStartMonth(accommodationSearchDto.getStart().getMonthValue())
                .setStartDay(accommodationSearchDto.getStart().getDayOfMonth())
                .setEndYear(accommodationSearchDto.getEnd().getYear())
                .setEndMonth(accommodationSearchDto.getEnd().getMonthValue())
                .setEndDay(accommodationSearchDto.getEnd().getDayOfMonth())
                .build();
        ListAccommodation accommodations = blockingStub.search(request);
        List<Accommodation> retVal = new ArrayList<>();
        for (communication.AccommodationFull a: accommodations.getAccommodationsList()) {
            retVal.add(convertAccommodationGrpcToAccommodation(a));
        }
        return retVal;
    }

    public List<Accommodation> findAllByUser(Long userId) {
        AccommodationServiceGrpc.AccommodationServiceBlockingStub blockingStub = getStub();
        ListAccommodation accommodations = blockingStub.findAllByUser(UserId.newBuilder().setUserId(userId).build());
        List<Accommodation> retVal = new ArrayList<>();
        for (communication.AccommodationFull a: accommodations.getAccommodationsList()) {
            retVal.add(convertAccommodationGrpcToAccommodation(a));
        }
        return retVal;
    }

    public String addAccommodation(Accommodation accommodation) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094)
                .usePlaintext()
                .build();

        AccommodationServiceGrpc.AccommodationServiceBlockingStub blockingStub = AccommodationServiceGrpc.newBlockingStub(channel);
        AccommodationFull request = AccommodationMapper.convertAccommodationToAccommodationGrpc(accommodation);

        MessageResponse response = blockingStub.addAccommodation(request);
        return response.getMessage();
    }

    public List<Accommodation> findAll() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094)
                .usePlaintext()
                .build();
        AccommodationServiceGrpc.AccommodationServiceBlockingStub blockingStub = AccommodationServiceGrpc.newBlockingStub(channel);
        ListAccommodation accommodations = blockingStub.findAll(Empty.newBuilder().build());
        List<Accommodation> retVal = new ArrayList<>();
        for(communication.AccommodationFull acc : accommodations.getAccommodationsList()){
            retVal.add(convertAccommodationGrpcToAccommodation(acc));
        }
        return retVal;
    }
}
