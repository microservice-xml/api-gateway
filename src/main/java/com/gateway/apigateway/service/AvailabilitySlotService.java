package com.gateway.apigateway.service;

import com.gateway.apigateway.model.AvailabilitySlot;
import communication.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.gateway.apigateway.mapper.AvailabilitySlotMapper.convertAvailabilitySlotGrpcToAvailabilitySlot;
import static com.gateway.apigateway.mapper.AvailabilitySlotMapper.convertAvailabilitySlotToAvailabilitySlotGrpc;

@Service
@RequiredArgsConstructor
public class AvailabilitySlotService {

    @Value("${reservation-api.grpc.address}")
    private String reservationApiGrpcAddress;

    private Logger logger = LoggerFactory.getLogger(AvailabilitySlotService.class);
    private AvailabilitySlotServiceGrpc.AvailabilitySlotServiceBlockingStub getStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        return AvailabilitySlotServiceGrpc.newBlockingStub(channel);
    }

    public List<com.gateway.apigateway.model.AvailabilitySlot> getAllByAccommodationId(Long accommodationId) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        AvailabilitySlotServiceGrpc.AvailabilitySlotServiceBlockingStub blockingStub = AvailabilitySlotServiceGrpc.newBlockingStub(channel);
        ListAvailabilitySlotFull listAvailabilitySlotFull = blockingStub.findAllAvailabilitySlotsByAccommodationId(LongId.newBuilder().setId(accommodationId).build());
        List<com.gateway.apigateway.model.AvailabilitySlot> retVal = new ArrayList<>();
        for (communication.AvailabilitySlotFull a: listAvailabilitySlotFull.getAvailabilitySlotsList()) {
            retVal.add(convertAvailabilitySlotGrpcToAvailabilitySlot(a));
        }
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return retVal;
    }

    public void add(AvailabilitySlot availabilitySlot) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();

        logger.info("Request for create new availability slot for AccommodationID:"+availabilitySlot.getId()+".");
        AvailabilitySlotServiceGrpc.AvailabilitySlotServiceBlockingStub blockingStub = AvailabilitySlotServiceGrpc.newBlockingStub(channel);
        availabilitySlot.setId("");
        EmptyMessage emptyMessage = blockingStub.add(convertAvailabilitySlotToAvailabilitySlotGrpc(availabilitySlot));
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }

    public void edit(AvailabilitySlot availabilitySlot) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                .usePlaintext()
                .build();
        logger.info("Request for edit availability slot.[ID:"+availabilitySlot.getId()+"]");
        AvailabilitySlotServiceGrpc.AvailabilitySlotServiceBlockingStub blockingStub = AvailabilitySlotServiceGrpc.newBlockingStub(channel);
        EmptyMessage emptyMessage = blockingStub.edit(convertAvailabilitySlotToAvailabilitySlotGrpc(availabilitySlot));
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }
}
