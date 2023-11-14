package com.pumpprogress.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            if (user == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(user);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty())
                return ResponseEntity.badRequest().build();
            if (userService.getUserByEmail(user.getEmail()) != null)
                return ResponseEntity.status(409).build();
            User userCreated = userService.addUser(user);
            return ResponseEntity.created(null).body(userCreated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add/admin")
    public ResponseEntity<User> addAdmin(@RequestBody User user) {
        try {
            if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty())
                return ResponseEntity.badRequest().build();
            if (userService.getUserByEmail(user.getEmail()) != null)
                return ResponseEntity.status(409).build();
            User userCreated = userService.addAdmin(user);
            return ResponseEntity.created(null).body(userCreated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            int idNum = Integer.parseInt(id);
            User userToUpdate = userService.getUserById(idNum);
            if (userToUpdate == null)
                return ResponseEntity.notFound().build();
            if (user.getName() != null)
                userToUpdate.setName(user.getName());
            if (user.getEmail() != null)
                userToUpdate.setEmail(user.getEmail());
            if (user.getPassword() != null)
                userToUpdate.setPassword(userService.cryptPassword(user.getPassword()));
            User userUpdated = userService.updateUser(userToUpdate);
            return ResponseEntity.ok(userUpdated);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/update/{id}/addUserRole")
    public ResponseEntity<User> addUserRole(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            User userToUpdate = userService.getUserById(idNum);
            if (userToUpdate == null)
                return ResponseEntity.notFound().build();
            userToUpdate = userService.addUserRole(userToUpdate);
            return ResponseEntity.ok(userToUpdate);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/update/{id}/addAdminRole")
    public ResponseEntity<User> addAdminRole(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            User userToUpdate = userService.getUserById(idNum);
            if (userToUpdate == null)
                return ResponseEntity.notFound().build();
            userToUpdate = userService.addAdminRole(userToUpdate);
            return ResponseEntity.ok(userToUpdate);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/update/{id}/removeAdminRole")
    public ResponseEntity<User> removeAdminRole(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            User userToUpdate = userService.getUserById(idNum);
            if (userToUpdate == null)
                return ResponseEntity.notFound().build();
            userToUpdate = userService.removeAdminRole(userToUpdate);
            return ResponseEntity.ok(userToUpdate);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/update/{id}/removeUserRole")
    public ResponseEntity<User> removeUserRole(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            User userToUpdate = userService.getUserById(idNum);
            if (userToUpdate == null)
                return ResponseEntity.notFound().build();
            userToUpdate = userService.removeUserRole(userToUpdate);
            return ResponseEntity.ok(userToUpdate);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            User user = userService.getUserById(idNum);
            if (user == null)
                return ResponseEntity.notFound().build();
            userService.deleteUser(user);
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}/admin")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String id) {
        try {
            int idNum = Integer.parseInt(id);
            User user = userService.getUserById(idNum);
            if (user == null)
                return ResponseEntity.notFound().build();
            userService.deleteUser(user);
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
