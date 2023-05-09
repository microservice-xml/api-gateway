package com.gateway.apigateway.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {

    @Email
    @NotBlank(message="Please enter your email.")
    String email;
    @NotBlank(message="Please enter your password.")
    String password;
}
