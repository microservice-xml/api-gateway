package com.gateway.apigateway.controller;

import com.gateway.apigateway.auth.AuthenticationRequest;
import com.gateway.apigateway.auth.AuthenticationResponse;
import com.gateway.apigateway.model.User;
import com.gateway.apigateway.security.jwt.JwtService;
import com.gateway.apigateway.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
            logger.trace("Request to log in with username {} was made", request.getUsername());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var user = authenticationService.loadUserByUsername(request.getUsername());
            var jwtToken = jwtService.generateToken(user);
            var authResponse = AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
            return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user){
            logger.trace("Request to register with username {} was made", user.getUsername());
            try{
                return ResponseEntity.status(HttpStatus.OK).body(authenticationService.register(user));
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Register user failed.");
            }
    }

    @GetMapping("/")
    @Secured({ "HOST" })
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok(passwordEncoder.encode("123.Auth"));
    }
}
