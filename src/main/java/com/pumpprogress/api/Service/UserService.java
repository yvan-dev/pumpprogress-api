package com.pumpprogress.api.Service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.pumpprogress.api.Model.Role;
import com.pumpprogress.api.Model.User;
import com.pumpprogress.api.Repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public String cryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User addUser(User user) {
        user.setPassword(cryptPassword(user.getPassword()));
        return userRepository.save(user);
    }

    public User addAdmin(User user) {
        Set<Role> roles = user.getRoles();
        roles.add(new Role(1, "ADMIN"));
        user.setPassword(cryptPassword(user.getPassword()));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User addAdminRole(User user) {
        Set<Role> roles = user.getRoles();
        roles.add(new Role(1, "ADMIN"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User addUserRole(User user) {
        Set<Role> roles = user.getRoles();
        roles.add(new Role(2, "USER"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User removeAdminRole(User user) {
        Set<Role> roles = user.getRoles();
        roles.remove(new Role(1, "ADMIN"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User removeUserRole(User user) {
        Set<Role> roles = user.getRoles();
        roles.remove(new Role(2, "USER"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
