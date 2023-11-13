package com.pumpprogress.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<User> authenticate(@RequestBody Credential credential) {
        try {
            if (credential.getEmail().isEmpty() || credential.getPassword().isEmpty())
                return ResponseEntity.badRequest().build();
            User user = userService.getUserByEmail(credential.getEmail());
            if (user == null || !BCrypt.checkpw(credential.getPassword(), user.getPassword()))
                return ResponseEntity.status(401).build();
            user.setToken(jwtTokenUtil.generateToken(user.getEmail()));
            userService.updateUser(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}