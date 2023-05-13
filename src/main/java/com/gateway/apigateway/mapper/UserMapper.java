package com.gateway.apigateway.mapper;

import com.gateway.apigateway.auth.RegistrationRequest;
import com.gateway.apigateway.model.User;
import communication.RegisterUser;
import communication.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public static RegisterUser convertToRegistrationRequest(User user) {
        RegisterUser request = RegisterUser.newBuilder()
                .setLocation(user.getLocation())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPhoneNumber(user.getPhoneNumber())
                .setPenalties(user.getPenalties())
                .setRole(user.getRole().equals(com.gateway.apigateway.model.Role.GUEST) ? Role.GUEST : Role.HOST)
                .build();
        return request;
    }
}
