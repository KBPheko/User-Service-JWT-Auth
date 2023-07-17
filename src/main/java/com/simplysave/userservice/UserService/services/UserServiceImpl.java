package com.simplysave.userservice.UserService.services;

import com.simplysave.userservice.UserService.DTO.LoginDto;
import com.simplysave.userservice.UserService.DTO.RegistrationDto;
import com.simplysave.userservice.UserService.models.Role;
import com.simplysave.userservice.UserService.models.User;
import com.simplysave.userservice.UserService.repositories.RoleRepository;
import com.simplysave.userservice.UserService.repositories.UserRepository;
import com.simplysave.userservice.UserService.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    //Constructor
    public UserServiceImpl(
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    //Login
    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    //Register
    @Override
    public void register(RegistrationDto registrationDto) {
        // Create a new user entity and set the necessary details
        User user = new User();

        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(registrationDto.getPassword());
        user.setPassword(encodedPassword);
        user.setCellphoneNumber(registrationDto.getCellphoneNumber());
       //Date
        user.setCreatedAt(registrationDto.getCreatedAt());
        user.setUpdatedAt(registrationDto.getUpdatedAt());

        // Assign ROLE_STUDENT to new registered user
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_STUDENT");
        Role userRole = optionalRole.orElseThrow(() -> new IllegalStateException("Role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Save the new user to the database
        userRepository.save(user);
    }

    @Override
    public void initiatePasswordReset(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email Not Found"));

        // Generate a password reset token (e.g., UUID)
        String resetToken = UUID.randomUUID().toString();

        // Save the reset token to the user entity
        user.setResetToken(resetToken);
        userRepository.save(user);

        // Send the password reset email to the user with the reset token
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

    }

    @Override
    public void resetPassword(String resetToken, String newPassword) {
        User user = userRepository.findByResetToken(resetToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

        // Set the new password and clear the reset token
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetToken(null);
        userRepository.save(user);
    }
}
