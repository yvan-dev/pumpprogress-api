package com.pumpprogress.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pumpprogress.api.Exception.UserNotFoundException;
import com.pumpprogress.api.Model.Credential;
import com.pumpprogress.api.Model.User;
import com.pumpprogress.api.Service.JwtTokenUtil;
import com.pumpprogress.api.Service.UserService;

@RestController
@RequestMapping("/api/authenticate")
public class AuthenticateController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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