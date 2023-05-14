package com.gateway.apigateway.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccommodationMapper {
    public static com.gateway.apigateway.model.Accommodation convertAccommodationGrpcToAccommodation(communication.AccommodationFull accommodation){
        return com.gateway.apigateway.model.Accommodation.builder()
                .id(accommodation.getId())
                .name(accommodation.getName())
                .location(accommodation.getLocation())
                .facilities(accommodation.getFacilities())
                .photo(accommodation.getPhoto())
                .minGuests(accommodation.getMinGuests())
                .maxGuests(accommodation.getMaxGuests())
                .availableBeds(accommodation.getAvailableBeds())
                .accommodationGradeId(accommodation.getAccommodationGradeId())
                .isAuto(accommodation.getIsAuto())
                .userId(accommodation.getUserId())
                .build();
    }

}
