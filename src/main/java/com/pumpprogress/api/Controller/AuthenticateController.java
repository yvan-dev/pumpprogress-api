package com.pumpprogress.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pumpprogress.api.exception.UserNotFoundException;
import com.pumpprogress.api.model.Credential;
import com.pumpprogress.api.model.User;
import com.pumpprogress.api.service.JwtTokenUtil;
import com.pumpprogress.api.service.UserService;

@RestController
@RequestMapping("/api/authenticate")
public class AuthenticateController {

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticateController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping
    public ResponseEntity<User> authenticate(@RequestBody @Validated Credential credential) {
        try {
            User user = userService.getUserByEmail(credential.getEmail());
            userService.checkPassword(credential.getPassword(), user.getPassword());
            String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRoles());
            user = userService.updateToken(user.getId(), token);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(401).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}