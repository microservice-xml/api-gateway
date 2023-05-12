package com.gateway.apigateway.service;

import com.gateway.apigateway.dto.UserDetailsResponseDto;
import com.gateway.apigateway.exception.UserNotFoundException;
import com.gateway.apigateway.model.User;
import communication.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final String url = "http://localhost:8083/api/user/user-details/";

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
          return getUserDetails(username);
            } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private User getUserDetails(String username){

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();

        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);
        UserDetailsRequest req = UserDetailsRequest.newBuilder().setUsername(username).build();
        UserDetailsResponse response = blockingStub.getUserDetails(req);
        User user = User.builder()
                .id(response.getId())
                .username(response.getUsername())
                .password(response.getPassword())
                .role(response.getRole().equals(Role.GUEST) ? com.gateway.apigateway.model.Role.GUEST : com.gateway.apigateway.model.Role.HOST)
                .penalties(response.getPenalties()).build();
        return user;
    }

    public String register(User user) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093)
                .usePlaintext()
                .build();

        userDetailsServiceGrpc.userDetailsServiceBlockingStub blockingStub = userDetailsServiceGrpc.newBlockingStub(channel);
        RegisterUser request = RegisterUser.newBuilder()
                .setLocation(user.getLocation())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setPassword(passwordEncoder.encode(user.getPassword()))
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPhoneNumber(user.getPhoneNumber())
                .setPenalties(user.getPenalties())
                .setRole(user.getRole().equals(com.gateway.apigateway.model.Role.GUEST) ? Role.GUEST : Role.HOST)
                .build();

        MessageResponse response = blockingStub.register(request);
        return response.getMessage();
    }

}
