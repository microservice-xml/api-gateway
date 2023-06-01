package com.gateway.apigateway.mapper;

import com.gateway.apigateway.model.Accommodation;
import com.gateway.apigateway.model.User;
import communication.AccommodationFull;
import communication.RegisterUser;
import communication.Role;
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

    public static Accommodation convertAccommodationGrpcToAccommodationWithGrade(communication.AccommodationWithGrade accommodation){
        return Accommodation.builder()
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
                .avgGrade(accommodation.getAvgGrade())
                .build();
    }

    public static AccommodationFull convertAccommodationToAccommodationGrpc(Accommodation accommodation) {
        AccommodationFull request = AccommodationFull.newBuilder()
                .setLocation(accommodation.getLocation())
                .setFacilities(accommodation.getFacilities())
                .setName(accommodation.getName())
                .setPhoto(accommodation.getPhoto())
                .setMaxGuests(accommodation.getMaxGuests())
                .setMinGuests(accommodation.getMinGuests())
                .setAvailableBeds(accommodation.getAvailableBeds())
                .setAccommodationGradeId(accommodation.getAccommodationGradeId())
                .setIsAuto((accommodation.isAuto()))
                .setUserId(accommodation.getUserId())
                .build();
        return request;
    }

}
