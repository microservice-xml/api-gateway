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

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private AccommodationServiceGrpc.AccommodationServiceBlockingStub getStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094)
                .usePlaintext()
                .build();
        return AccommodationServiceGrpc.newBlockingStub(channel);
    }

    public List<Accommodation> findAll() {
        return null;
    }

    public List<Accommodation> search(AccommodationSearchDto accommodationSearchDto) {
        return null;
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
}
