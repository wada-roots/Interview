package com.kk.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kk.users.entity.User;
import com.kk.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Save a new user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Get user by ID
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    // Update an existing user
    public User updateUser(int id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));

        // Update fields
        existingUser.setFullName(user.getFullName());
        existingUser.setOccupation(user.getOccupation());
        existingUser.setEmail(user.getEmail());

        return userRepository.save(existingUser);
    }

    // Delete a user by ID
    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));

        userRepository.delete(user);
    }

    // Search users by keyword (name or occupation)
    public List<User> searchUsers(String keyword) {
        return userRepository.search(keyword);
    }
}
