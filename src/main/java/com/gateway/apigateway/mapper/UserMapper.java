package com.gateway.apigateway.mapper;

import com.gateway.apigateway.auth.RegistrationRequest;
import com.gateway.apigateway.dto.User.UserDto;
import com.gateway.apigateway.model.User;
import communication.RegisterUser;
import communication.RegisterUserAvgGrade;
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
        communication.User request;
        if (user.getPassword() == null){
            request = communication.User.newBuilder()
                    .setId(user.getId())
                    .setLocation(user.getLocation())
                    .setEmail(user.getEmail())
                    .setUsername(user.getUsername())
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setPhoneNumber(user.getPhoneNumber())
                    .setRole(convertToMessageRole(user.getRole()))
                    .setPenalties(user.getPenalties())
                    .build();
        } else {
            request = communication.User.newBuilder()
                    .setId(user.getId())
                    .setLocation(user.getLocation())
                    .setEmail(user.getEmail())
                    .setUsername(user.getUsername())
                    .setFirstName(user.getFirstName())
                    .setPassword(user.getPassword())
                    .setLastName(user.getLastName())
                    .setPhoneNumber(user.getPhoneNumber())
                    .setRole(convertToMessageRole(user.getRole()))
                    .setPenalties(user.getPenalties())
                    .build();
        }

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
                .role(convertToEntityRole(communicationUser.getRole()))
                .build();
    }

    public static List<User> convertUsersGrpcToUsers(UserList communicationUserList) {
        List<User> userList = new ArrayList<>();

        for (communication.User communicationUser : communicationUserList.getUsersList()) {
            User user = convertUserGrpcToUser(communicationUser);
            userList.add(user);
        }
        return userList;
    }
    public static UserDto convertFromMessageToUserDto(RegisterUser user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .password(user.getPassword())
                .location(user.getLocation())
                .role(convertToEntityRole(user.getRole()))
                .username(user.getUsername())
                .build();
    }

    public static UserDto convertFromMessageToUserDto(RegisterUserAvgGrade user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .password(user.getPassword())
                .location(user.getLocation())
                .role(convertToEntityRole(user.getRole()))
                .username(user.getUsername())
                .avgGrade(user.getAvgGrade())
                .build();
    }
}
