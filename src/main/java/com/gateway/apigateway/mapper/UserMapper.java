package com.gateway.apigateway.mapper;

import com.gateway.apigateway.auth.RegistrationRequest;
import com.gateway.apigateway.model.User;
import communication.RegisterUser;
import communication.Role;
import communication.UserList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private static com.gateway.apigateway.model.Role convertToEntityRole (Role role) {
        return role.equals(Role.GUEST) ? com.gateway.apigateway.model.Role.GUEST : com.gateway.apigateway.model.Role.HOST;
    }

    private static Role convertToMessageRole (com.gateway.apigateway.model.Role role) {
        return role.equals(com.gateway.apigateway.model.Role.GUEST) ?  Role.GUEST: Role.HOST;
    }

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

    public static communication.User convertUserToUserGrpc(User user) {
        communication.User request = communication.User.newBuilder()
                .setId(user.getId())
                .setLocation(user.getLocation())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPhoneNumber(user.getPhoneNumber())
                .setPenalties(user.getPenalties())
                .build();
        return request;
    }

    public static User convertUserGrpcToUser(communication.User communicationUser) {
        return User.builder()
                .id(communicationUser.getId())
                .location(communicationUser.getLocation())
                .email(communicationUser.getEmail())
                .username(communicationUser.getUsername())
                .firstName(communicationUser.getFirstName())
                .lastName(communicationUser.getLastName())
                .phoneNumber(communicationUser.getPhoneNumber())
                .penalties(communicationUser.getPenalties())
                .password(communicationUser.getPassword())
                .build();
    }

    public static List<User> convertUsersGrpcToUsers(UserList communicationUserList) {
        List<User> userList = new ArrayList<>();

        for (communication.User communicationUser : communicationUserList.getUsersList()) {
            User user = User.builder()
                    .id(communicationUser.getId())
                    .location(communicationUser.getLocation())
                    .email(communicationUser.getEmail())
                    .username(communicationUser.getUsername())
                    .firstName(communicationUser.getFirstName())
                    .lastName(communicationUser.getLastName())
                    .phoneNumber(communicationUser.getPhoneNumber())
                    .penalties(communicationUser.getPenalties())
                    .password(communicationUser.getPassword())
                    .build();
            userList.add(user);
        }
        return userList;
    }
}
