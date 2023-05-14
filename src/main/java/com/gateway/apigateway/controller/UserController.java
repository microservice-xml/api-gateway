package com.gateway.apigateway.controller;

import com.gateway.apigateway.dto.User.UserDto;
import com.gateway.apigateway.model.User;
import com.gateway.apigateway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/all")
    public ResponseEntity findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }
    @PutMapping("/change-personal-info")
    public ResponseEntity<User> changeUserInfo(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeUserInfo(user));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }
    /*
    @GetMapping("/user-details/{username}")
    public ResponseEntity<UserDetailsResponseDto> getUserDetails(@PathVariable("username") String username) {
        User user = userService.loadUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDetailsResponseDto dto = new UserDetailsResponseDto(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), user.getPenalties());
        return ResponseEntity.ok(dto);
    } */
}
