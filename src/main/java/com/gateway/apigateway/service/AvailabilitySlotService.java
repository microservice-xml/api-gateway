package com.gateway.apigateway.service;

import communication.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.gateway.apigateway.mapper.AvailabilitySlotMapper.convertAvailabilitySlotGrpcToAvailabilitySlot;

@Service
@RequiredArgsConstructor
public class AvailabilitySlotService {
    private AvailabilitySlotServiceGrpc.AvailabilitySlotServiceBlockingStub getStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9095)
                .usePlaintext()
                .build();
        return AvailabilitySlotServiceGrpc.newBlockingStub(channel);
    }

    public List<com.gateway.apigateway.model.AvailabilitySlot> getAllByAccommodationId(Long accommodationId) {
        AvailabilitySlotServiceGrpc.AvailabilitySlotServiceBlockingStub blockingStub = getStub();
        ListAvailabilitySlotFull listAvailabilitySlotFull = blockingStub.findAllAvailabilitySlotsByAccommodationId(LongId.newBuilder().setId(accommodationId).build());
        List<com.gateway.apigateway.model.AvailabilitySlot> retVal = new ArrayList<>();
        for (communication.AvailabilitySlotFull a: listAvailabilitySlotFull.getAvailabilitySlotsList()) {
            retVal.add(convertAvailabilitySlotGrpcToAvailabilitySlot(a));
        }
        return retVal;
    }
}
