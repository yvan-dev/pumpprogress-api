package com.pumpprogress.api.service;

import java.util.Set;


import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.pumpprogress.api.exception.UserEmailAlreadyExistException;
import com.pumpprogress.api.exception.UserNotFoundException;
import com.pumpprogress.api.model.Role;
import com.pumpprogress.api.model.User;
import com.pumpprogress.api.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String cryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void checkPassword(String password, String hash) {
        if (!BCrypt.checkpw(password, hash)) {
            throw new UserNotFoundException("Invalid credentials");
        }
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    public void checkEmailExist(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserEmailAlreadyExistException("Email already exists");
        }

    }

    public User addUser(User user) {
        checkEmailExist(user.getEmail());
        user.setPassword(cryptPassword(user.getPassword()));
        return userRepository.save(user);
    }

    public User addAdmin(User user) {
        checkEmailExist(user.getEmail());
        Set<Role> roles = user.getRoles();
        roles.add(new Role(1, "ADMIN"));
        user.setPassword(cryptPassword(user.getPassword()));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User addAdminRole(int id) {
        User user = getUserById(id);
        Set<Role> roles = user.getRoles();
        roles.add(new Role(1, "ADMIN"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User addUserRole(int id) {
        User user = getUserById(id);
        Set<Role> roles = user.getRoles();
        roles.add(new Role(2, "USER"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User removeAdminRole(int id) {
        User user = getUserById(id);
        Set<Role> roles = user.getRoles();
        roles.remove(new Role(1, "ADMIN"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User removeUserRole(int id) {
        User user = getUserById(id);
        Set<Role> roles = user.getRoles();
        roles.remove(new Role(2, "USER"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User updateUser(int id, User user) {
        User userToUpdate = getUserById(id);
        if (user.getName() != null)
            userToUpdate.setName(user.getName());
        if (user.getEmail() != null)
            userToUpdate.setEmail(user.getEmail());
        if (user.getPassword() != null)
            userToUpdate.setPassword(cryptPassword(user.getPassword()));
        return userRepository.save(userToUpdate);
    }

    public User updateToken(int id, String token) {
        User userToUpdate = getUserById(id);
        userToUpdate.setToken(token);
        return userRepository.save(userToUpdate);
    }

    public void deleteUser(int id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
