package com.codeelevate.stackoverflow_spring.controller;

import com.codeelevate.stackoverflow_spring.entity.User;
import com.codeelevate.stackoverflow_spring.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
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

    @GetMapping("/getUserIdByEmail")
    @ResponseBody
    public ResponseEntity<?> getUserIdByEmail(@RequestParam String email) {
        Integer userId = userService.findUserIdByEmail(email);

        if (userId != null) {
            return ResponseEntity.ok(Map.of("userId", userId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
    }

    @PutMapping("/updateUserById/{id}")
    @ResponseBody
    public User updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @DeleteMapping("/deleteById/{id}")
    @ResponseBody
    public void deleteUserById( @PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @GetMapping("/get/byUsername/{username}")
    @ResponseBody
    public List<User> getUserByUsername(@PathVariable String username) {
        return userService.findAllUsersByUsername(username);
    }

    @GetMapping("/get/byEmail/{email}")
    @ResponseBody
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        // Găsește utilizatorul după email
        Optional<User> optionalUser = userService.findUserByEmail(user.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or password."));
        }

        User authenticatedUser = optionalUser.get();

        // Verifică parola
        boolean isAuthenticated = userService.authenticateUser(authenticatedUser.getEmail(), user.getPassword());

        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or password."));
        }

        // ✅ Verificare dacă user-ul este banat
        if (authenticatedUser.getBanned()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied: You are banned."));
        }

        // ✅ Login reușit
        return ResponseEntity.ok(Map.of("message", "Login successful!"));
    }



    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        System.out.println("Received email: " + email);
        boolean exists = userService.emailExists(email);
        System.out.println("Email exists: " + exists);
        System.out.println("ResponseEntity: " + ResponseEntity.ok(exists));
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam String username) {
        boolean exists = userService.usernameExists(username);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/ban-user/{userId}")
    @ResponseBody
    public ResponseEntity<?> banUser(@PathVariable Integer userId) {
        userService.banUser(userId);
        return ResponseEntity.ok(Map.of("message", "User banned successfully"));
    }

    @PostMapping("/unban-user/{userId}")
    @ResponseBody
    public ResponseEntity<?> unbanUser(@PathVariable Integer userId) {
        userService.unbanUser(userId);
        return ResponseEntity.ok(Map.of("message", "User unbanned successfully"));
    }

    @GetMapping("/getUsernameById/{id}")
    @ResponseBody
    public ResponseEntity<?> getUsernameById(@PathVariable Integer id) {
        Optional<String> usernameOpt = userService.findUsernameById(id);
        if (usernameOpt.isPresent()) {
            return ResponseEntity.ok(Map.of("username", usernameOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
    }

}
