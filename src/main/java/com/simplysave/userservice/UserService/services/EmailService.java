package com.simplysave.userservice.UserService.services;

public interface EmailService {
    void sendPasswordResetEmail(String recipientEmail, String resetToken);
}
