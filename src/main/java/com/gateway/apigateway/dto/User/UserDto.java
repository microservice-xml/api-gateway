package com.gateway.apigateway.dto.User;

import com.gateway.apigateway.model.Role;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class UserDto {


    private String username;

    private String location;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;
    private String password;
    private Role role;
    private float avgGrade;
    private boolean isHighlighted;
}
