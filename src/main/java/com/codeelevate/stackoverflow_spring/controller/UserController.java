package com.codeelevate.stackoverflow_spring.controller;

import com.codeelevate.stackoverflow_spring.entity.User;
import com.codeelevate.stackoverflow_spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/getAllUsers")
    @ResponseBody
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/createUser")
    @ResponseBody
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/getUserById/{id}")
    @ResponseBody
    public Optional<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/updateUserById/{id}")
    @ResponseBody
    public User updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @DeleteMapping("/deleteById/{id}")
    @ResponseBody
    public void deleteUserById(@RequestParam @PathVariable Integer id) {
        userService.deleteUser(id);
    }



}
