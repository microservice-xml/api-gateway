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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import static com.gateway.apigateway.mapper.RateAccommodationMapper.convertFromMessageToRateWithId;
import static com.gateway.apigateway.mapper.RateMapper.convertRateRequestToEntity;
import static com.gateway.apigateway.mapper.RateMapper.convertRateRequestToEntityWithId;
import static com.gateway.apigateway.mapper.ReservationMapper.convertReservationGrpcToReservation;
import static com.gateway.apigateway.mapper.UserMapper.convertUserGrpcToUser;

@Service
@RequiredArgsConstructor
public class RateService {

    @Value("${accommodation-api.grpc.address}")
    private String accommodationApiGrpcAddress;

    @Value("${user-api.grpc.address}")
    private String userApiGrpcAddress;
    private Logger logger = LoggerFactory.getLogger(RateService.class);
    public String rateHost(Rate rate) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(userApiGrpcAddress, 9093)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);
        logger.info("Request for new rate host [ID: %d] by guest [ID: %d]",rate.getHostId(), rate.getGuestId());
        communication.Rate request = RateMapper.convertFromMessageToRate(rate);

        MessageResponse response = blockingStub.rateHost(request);
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return response.getMessage();
    }
    public Rate changeRate(Rate rate) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(userApiGrpcAddress, 9093)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);
        logger.info("Request for edit rate host [ID: %d] by guest [ID: %d]",rate.getHostId(), rate.getGuestId());
        communication.Rate r = RateMapper.convertFromMessageToRateWithId(rate);
        communication.Rate response = blockingStub.changeRate(r);
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return convertRateRequestToEntityWithId(response);
    }

    public Rate deleteRate(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(userApiGrpcAddress, 9093)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);
        logger.info("Request for remove rate. [ID %d]",id);
        communication.UserIdRequest request = communication.UserIdRequest.newBuilder()
                .setId(id)
                .build();

        communication.Rate response = blockingStub.deleteRate(request);
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return convertRateRequestToEntityWithId(response);
    }

    public String rateAccommodation(RateAccommodation rate) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(accommodationApiGrpcAddress, 9094)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);
        logger.info("Request for new rate accomodation by guest. [ID: %d]", rate.getGuestId());
        communication.AccommodationRate request = RateAccommodationMapper.convertFromMessageToRate(rate);

        MessageResponse response = blockingStub.rateAccommodation(request);
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return response.getMessage();
    }

    public RateAccommodation changeAccommodationRate(RateAccommodation rate) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(accommodationApiGrpcAddress, 9094)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);
        logger.info("Request for edit rate accomodation by guest. [ID: %d]", rate.getGuestId());
        communication.AccommodationRate r = convertFromMessageToRateWithId(rate);
        communication.AccommodationRate response = blockingStub.changeAccommodationRate(r);
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return RateAccommodationMapper.convertRateRequestToEntityWithId(response);
    }

    public RateAccommodation deleteAccommodationRate(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(accommodationApiGrpcAddress, 9094)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);
        logger.info("Request for remove rate accomodation. [ID: %d]", id);
        communication.UserIdRequest request = communication.UserIdRequest.newBuilder()
                .setId(id)
                .build();

        communication.AccommodationRate response = blockingStub.deleteAccommodationRate(request);
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return RateAccommodationMapper.convertRateRequestToEntityWithId(response);
    }

    public List<Rate> getAllByHostId(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(userApiGrpcAddress, 9093)
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
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return retVal;
    }

    public List<RateAccommodation> findAllByAccommodationId(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(accommodationApiGrpcAddress, 9094)
                .usePlaintext()
                .build();
        rateServiceGrpc.rateServiceBlockingStub blockingStub = rateServiceGrpc.newBlockingStub(channel);

        communication.UserIdRequest request = communication.UserIdRequest.newBuilder()
                .setId(id)
                .build();

        ListAccommodationRate rates = blockingStub.findAllByAccommodationId(request);
        List<RateAccommodation> retVal = new ArrayList<>();
        for (communication.AccommodationRate rate : rates.getAccommodationRatesList()) {
            retVal.add(RateAccommodationMapper.convertFromMessageToRateAccommodation(rate));
        }
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return retVal;
    }
}
