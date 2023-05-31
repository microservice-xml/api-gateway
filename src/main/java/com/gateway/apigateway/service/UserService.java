package com.gateway.apigateway.service;


import com.gateway.apigateway.model.User;
import communication.*;

import com.gateway.apigateway.dto.User.UserDto;
import com.gateway.apigateway.mapper.UserMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.gateway.apigateway.mapper.ReservationMapper.convertReservationGrpcToReservation;
import static com.gateway.apigateway.mapper.UserMapper.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    public User changeUserInfo(User user) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();
        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);
        if(user.getPassword()!=null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        communication.User u = convertUserToUserGrpc(user);
        communication.User response = blockingStub.changeUserInfo(u);
        return convertUserGrpcToUser(response);
    }

    public List<User> findAll() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();
        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);
        UserList users = blockingStub.findAll(EmptyRequest.newBuilder().build());
        List<User> retVal = new ArrayList<>();
        for(communication.User user : users.getUsersList()){
            retVal.add(convertUserGrpcToUser(user));
        }
        return retVal;
    }

    public String deleteUser(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();
        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);
        communication.MessageResponse message = blockingStub.delete(communication.UserIdRequest.newBuilder().setId(id).build());
        return message.getMessage();
    }
    public UserDto getById(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();
        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);

        communication.RegisterUserAvgGrade user = blockingStub.getById(communication.UserIdRequest.newBuilder().setId(id).build());

        return UserMapper.convertFromMessageToUserDto(user);
    }
}
