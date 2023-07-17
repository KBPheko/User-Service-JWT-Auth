package com.simplysave.userservice.UserService.services;

import com.simplysave.userservice.UserService.DTO.LoginDto;
import com.simplysave.userservice.UserService.DTO.RegistrationDto;

public interface UserService {

    //Login User DTO
    String login(LoginDto loginDto);

    //User Registration DTO
    void register(RegistrationDto registrationDto);

    // Forgot Password
    void initiatePasswordReset(String email);
    void resetPassword(String resetToken, String newPassword);
}
