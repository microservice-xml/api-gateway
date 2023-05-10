package com.gateway.apigateway.dto;

import com.gateway.apigateway.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class UserDetailsResponseDto implements Serializable {

    private Long id;

    private String email;

    private String password;

    private Role role;

    private int penalties;
}
