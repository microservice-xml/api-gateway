package com.gateway.apigateway.service;

 import com.gateway.apigateway.dto.User.UserDto;
import com.gateway.apigateway.mapper.AccommodationMapper;
import com.gateway.apigateway.mapper.UserMapper;
import com.gateway.apigateway.dto.AccommodationDto;
import com.gateway.apigateway.model.Accommodation;
import com.gateway.apigateway.dto.AccommodationSearchDto;
import communication.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.gateway.apigateway.mapper.AccommodationMapper.convertAccommodationGrpcToAccommodation;
import static com.gateway.apigateway.mapper.AccommodationMapper.convertAccommodationGrpcToAccommodationDto;
import static com.gateway.apigateway.mapper.ReservationMapper.convertReservationToReservationGrpc;
import static com.gateway.apigateway.mapper.UserMapper.convertUserGrpcToUser;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    @Value("${accommodation-api.grpc.address}")
    private String accommodationApiGrpcAddress;

//    @Autowired
//    public AccommodationService(@Value("${accommodation.api.grpc.address}") String accommodationApiGrpcAddress) {
//        this.accommodationApiGrpcAddress = accommodationApiGrpcAddress;
//    }

    private AccommodationServiceGrpc.AccommodationServiceBlockingStub getStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(accommodationApiGrpcAddress, 9094)
                .usePlaintext()
                .build();
        return AccommodationServiceGrpc.newBlockingStub(channel);
    }

    public List<AccommodationDto> search(AccommodationSearchDto accommodationSearchDto) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(accommodationApiGrpcAddress, 9094)
                .usePlaintext()
                .build();
        AccommodationServiceGrpc.AccommodationServiceBlockingStub blockingStub = AccommodationServiceGrpc.newBlockingStub(channel);
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
        List<AccommodationDto> retVal = new ArrayList<>();
        for (communication.AccommodationFull a: accommodations.getAccommodationsList()) {
            retVal.add(convertAccommodationGrpcToAccommodationDto(a));
        }
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return retVal;
    }

    public List<Accommodation> findAllByUser(Long userId) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(accommodationApiGrpcAddress, 9094)
                .usePlaintext()
                .build();
        AccommodationServiceGrpc.AccommodationServiceBlockingStub blockingStub = AccommodationServiceGrpc.newBlockingStub(channel);
        ListAccommodation accommodations = blockingStub.findAllByUser(UserId.newBuilder().setUserId(userId).build());
        List<Accommodation> retVal = new ArrayList<>();
        for (communication.AccommodationFull a: accommodations.getAccommodationsList()) {
            retVal.add(convertAccommodationGrpcToAccommodation(a));
        }
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return retVal;
    }

    public String addAccommodation(Accommodation accommodation) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(accommodationApiGrpcAddress, 9094)
                .usePlaintext()
                .build();

        AccommodationServiceGrpc.AccommodationServiceBlockingStub blockingStub = AccommodationServiceGrpc.newBlockingStub(channel);
        AccommodationFull request = AccommodationMapper.convertAccommodationToAccommodationGrpc(accommodation);

        MessageResponse response = blockingStub.addAccommodation(request);
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return response.getMessage();
    }

    public List<Accommodation> findAll() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(accommodationApiGrpcAddress, 9094)
                .usePlaintext()
                .build();
        AccommodationServiceGrpc.AccommodationServiceBlockingStub blockingStub = AccommodationServiceGrpc.newBlockingStub(channel);
        ListAccommodation accommodations = blockingStub.findAll(Empty.newBuilder().build());
        List<Accommodation> retVal = new ArrayList<>();
        for(communication.AccommodationFull acc : accommodations.getAccommodationsList()){
            retVal.add(convertAccommodationGrpcToAccommodation(acc));
        }
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return retVal;
    }

    public Accommodation findById(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(accommodationApiGrpcAddress, 9094)
                .usePlaintext()
                .build();
        AccommodationServiceGrpc.AccommodationServiceBlockingStub blockingStub = AccommodationServiceGrpc.newBlockingStub(channel);

        communication.AccommodationWithGrade acc = blockingStub.findById(communication.UserIdRequest.newBuilder().setId(id).build());

        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return AccommodationMapper.convertAccommodationGrpcToAccommodationWithGrade(acc);
    }
}
