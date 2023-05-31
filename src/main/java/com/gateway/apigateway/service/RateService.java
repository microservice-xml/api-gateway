package com.gateway.apigateway.service;

import com.gateway.apigateway.mapper.RateAccommodationMapper;
import com.gateway.apigateway.mapper.RateMapper;
import com.gateway.apigateway.model.Rate;
import com.gateway.apigateway.model.RateAccommodation;
import com.gateway.apigateway.model.Reservation;
import communication.*;
import communication.AccommodationRate;
import communication.Id;
import communication.MessageResponse;
import communication.rateServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import static com.gateway.apigateway.mapper.RateMapper.convertRateRequestToEntity;
import static com.gateway.apigateway.mapper.RateMapper.convertRateRequestToEntityWithId;
import static com.gateway.apigateway.mapper.ReservationMapper.convertReservationGrpcToReservation;
import static com.gateway.apigateway.mapper.UserMapper.convertUserGrpcToUser;

@Service
@RequiredArgsConstructor
public class RateService {

    public String rateHost(Rate rate) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);

        communication.Rate request = RateMapper.convertFromMessageToRate(rate);

        MessageResponse response = blockingStub.rateHost(request);
        return response.getMessage();
    }
    public Rate changeRate(Rate rate) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);

        communication.Rate r = RateMapper.convertFromMessageToRateWithId(rate);
        communication.Rate response = blockingStub.changeRate(r);
        return convertRateRequestToEntityWithId(response);
    }

    public Rate deleteRate(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);

        communication.UserIdRequest request = communication.UserIdRequest.newBuilder()
                .setId(id)
                .build();

        communication.Rate response = blockingStub.deleteRate(request);
        return convertRateRequestToEntityWithId(response);
    }

    public String rateAccommodation(RateAccommodation rate) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);

        communication.AccommodationRate request = RateAccommodationMapper.convertFromMessageToRate(rate);

        MessageResponse response = blockingStub.rateAccommodation(request);
        return response.getMessage();
    }

    public RateAccommodation changeAccommodationRate(RateAccommodation rate) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);

        communication.AccommodationRate r = RateAccommodationMapper.convertFromMessageToRateWithId(rate);
        communication.AccommodationRate response = blockingStub.changeAccommodationRate(r);
        return RateAccommodationMapper.convertRateRequestToEntityWithId(response);
    }

    public RateAccommodation deleteAccommodationRate(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);

        communication.UserIdRequest request = communication.UserIdRequest.newBuilder()
                .setId(id)
                .build();

        communication.AccommodationRate response = blockingStub.deleteAccommodationRate(request);
        return RateAccommodationMapper.convertRateRequestToEntityWithId(response);
    }

    public List<Rate> getAllByHostId(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);

        communication.UserIdRequest request = communication.UserIdRequest.newBuilder()
                .setId(id)
                .build();

        ListRate rates = blockingStub.getAllByHostId(request);
        List<Rate> retVal = new ArrayList<>();
        for(communication.Rate rate : rates.getRatesList()){
            retVal.add(convertRateRequestToEntityWithId(rate));
        }
        return retVal;
    }
}
