package com.gateway.apigateway.service;


import com.gateway.apigateway.model.User;
import communication.*;

import com.gateway.apigateway.dto.User.UserDto;
import com.gateway.apigateway.mapper.UserMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.gateway.apigateway.mapper.ReservationMapper.convertReservationGrpcToReservation;
import static com.gateway.apigateway.mapper.UserMapper.*;

@Service
//@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

//    @Value("${user-api.grpc.address}")
    private final String userApiGrpcAddress;

    @Autowired
    public UserService(@Value("${user-api.grpc.address}") String userApiGrpcAddress, PasswordEncoder passwordEncoder) {
        this.userApiGrpcAddress = userApiGrpcAddress;
        this.passwordEncoder = passwordEncoder;
    }

//    @Autowired
//    private final Environment environment;
//
//    // Access the property value
//    String userApiGrpcAddress = environment.getProperty("user-api.grpc.address");

    public User changeUserInfo(User user) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(userApiGrpcAddress, 9093)
                .usePlaintext()
                .build();
        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);
        if(user.getPassword()!=null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        communication.User u = convertUserToUserGrpc(user);
        communication.User response = blockingStub.changeUserInfo(u);
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return convertUserGrpcToUser(response);
    }

    public List<User> findAll() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(userApiGrpcAddress, 9093)
                .usePlaintext()
                .build();
        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);
        UserList finaListUsers = blockingStub.findAll(EmptyRequest.newBuilder().build());
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return convertUsersExtendedGrpcToUsers(finaListUsers);
    }

    public String deleteUser(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(userApiGrpcAddress, 9093)
                .usePlaintext()
                .build();
        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);
        communication.MessageResponse message = blockingStub.delete(communication.UserIdRequest.newBuilder().setId(id).build());
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return message.getMessage();
    }
    public UserDto getById(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(userApiGrpcAddress, 9093)
                .usePlaintext()
                .build();
        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);

        communication.RegisterUserAvgGrade user = blockingStub.getById(communication.UserIdRequest.newBuilder().setId(id).build());

        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
        return UserMapper.convertFromMessageToUserDto(user);
    }
}
