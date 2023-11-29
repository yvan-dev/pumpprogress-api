package com.pumpprogress.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pumpprogress.api.Model.User;
import com.pumpprogress.api.Service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            User user = userService.getUserById(idNum);
            return ResponseEntity.ok(user);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody @Validated User user) {
        User userCreated = userService.addUser(user);
        return ResponseEntity.created(null).body(userCreated);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<User> addAdmin(@RequestBody @Validated User user) {
            User userCreated = userService.addAdmin(user);
            return ResponseEntity.created(null).body(userCreated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            int idNum = Integer.parseInt(id);
            User userUpdated = userService.updateUser(idNum, user);
            return ResponseEntity.ok(userUpdated);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/addUserRole")
    public ResponseEntity<User> addUserRole(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            User userToUpdate = userService.addUserRole(idNum);
            return ResponseEntity.ok(userToUpdate);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/addAdminRole")
    public ResponseEntity<User> addAdminRole(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            User userToUpdate = userService.addAdminRole(idNum);
            return ResponseEntity.ok(userToUpdate);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/removeAdminRole")
    public ResponseEntity<User> removeAdminRole(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            User userToUpdate = userService.removeAdminRole(idNum);
            return ResponseEntity.ok(userToUpdate);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/removeUserRole")
    public ResponseEntity<User> removeUserRole(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            User userToUpdate = userService.removeUserRole(idNum);
            return ResponseEntity.ok(userToUpdate);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            userService.deleteUser(idNum);
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}/admin")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            userService.deleteUser(idNum);
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
