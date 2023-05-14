package com.gateway.apigateway.service;


import com.gateway.apigateway.model.User;
import communication.EmptyRequest;
import communication.UserList;
import communication.userDetailsServiceGrpc;

import com.gateway.apigateway.dto.User.UserDto;
import com.gateway.apigateway.mapper.UserMapper;
import communication.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        UserList finaListUsers = blockingStub.findAll(EmptyRequest.newBuilder().build());

        return convertUsersGrpcToUsers(finaListUsers);
    }

    public String deleteUser(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();
        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);
        MessageResponse message = blockingStub.delete(UserIdRequest.newBuilder().setId(id).build());
        return message.getMessage();
    }
    public UserDto getById(Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();
        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);

        RegisterUser user = blockingStub.getById(UserIdRequest.newBuilder().setId(id).build());

        return UserMapper.convertFromMessageToUserDto(user);
    }
}
