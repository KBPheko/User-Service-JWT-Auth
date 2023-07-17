package com.simplysave.userservice.UserService.controllers;

import com.simplysave.userservice.UserService.DTO.LoginDto;
import com.simplysave.userservice.UserService.DTO.RegistrationDto;
import com.simplysave.userservice.UserService.repositories.UserRepository;
import com.simplysave.userservice.UserService.security.JwtAuthResponse;
import com.simplysave.userservice.UserService.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationDto registrationDto){
        userService.register(registrationDto);
        return ResponseEntity.ok("Registration successful");
    }

    // Build Login REST API
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticate(@RequestBody LoginDto loginDto){
        String token = userService.login(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    //Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        userService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset initiated");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("resetToken") String resetToken,
                                           @RequestParam("newPassword") String newPassword) {
        userService.resetPassword(resetToken, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }
}
