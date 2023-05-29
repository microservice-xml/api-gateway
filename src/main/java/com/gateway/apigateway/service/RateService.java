package com.gateway.apigateway.service;

import com.gateway.apigateway.mapper.RateMapper;
import com.gateway.apigateway.model.Rate;
import communication.Id;
import communication.MessageResponse;
import communication.rateServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
