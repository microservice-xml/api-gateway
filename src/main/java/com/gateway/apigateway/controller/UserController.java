package com.gateway.apigateway.controller;

import com.gateway.apigateway.dto.User.UserDto;
import com.gateway.apigateway.model.User;
import com.gateway.apigateway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/all")
    //@Secured({ "HOST" })
    public ResponseEntity findAll() {
        logger.trace("Request to find all users was made");
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }
    @PutMapping("/change-personal-info")
    public ResponseEntity<User> changeUserInfo(@RequestBody User user) {
        logger.trace("Request to change user info for user with id {} was made", user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeUserInfo(user));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") Long id) {
        logger.trace("Request to find the user with id {} was made", id);
        return ResponseEntity.ok(userService.getById(id));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        logger.trace("Request to delete the user with id {} was made", id);
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
