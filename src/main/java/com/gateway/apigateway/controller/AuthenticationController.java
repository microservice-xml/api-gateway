package com.gateway.apigateway.controller;

import com.gateway.apigateway.auth.AuthenticationRequest;
import com.gateway.apigateway.auth.AuthenticationResponse;
import com.gateway.apigateway.auth.RegistrationRequest;
import com.gateway.apigateway.security.jwt.JwtService;
import com.gateway.apigateway.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var user = authenticationService.loadUserByUsername(request.getUsername());
            var jwtToken = jwtService.generateToken(user);
            var authResponse = AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
            return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/")
    public ResponseEntity<String> helloWorld() {

        return ResponseEntity.ok(new BCryptPasswordEncoder().encode("123.Auth"));
    }
}
