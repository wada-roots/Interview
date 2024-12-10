package com.kk.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kk.users.entity.User;
import com.kk.users.service.UserService;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Render the login page


    // Display all users
    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users"; // Render users.html
    }

    // Display the "Create User" form
    @GetMapping("/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute("user") User user, Model model) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    // Display the "Edit User" form
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") int id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        model.addAttribute("user", user);
        return "editUser"; // Render editUser.html
    }

    // Handle the "Edit User" form submission
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id,
@Valid @ModelAttribute("user") User user, Model model) {
        userService.updateUser(id, user); // Update the user
        return "redirect:/users";
    }

    // Delete a user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id); // Delete the user
        return "redirect:/users";
    }

    // Search users by full name or occupation
    @GetMapping("/search")
    public String searchUsers(@RequestParam String keyword, Model model) {
        List<User> users = userService.searchUsers(keyword);
        model.addAttribute("users", users);
        return "users";
    }
}
