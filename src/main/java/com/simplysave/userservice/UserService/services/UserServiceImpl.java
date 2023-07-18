package com.simplysave.userservice.UserService.services;

import com.simplysave.userservice.UserService.DTO.LoginDto;
import com.simplysave.userservice.UserService.DTO.RegistrationDto;
import com.simplysave.userservice.UserService.models.Admin;
import com.simplysave.userservice.UserService.models.Role;
import com.simplysave.userservice.UserService.models.Student;
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

import java.util.*;

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
       // User user = new User();
        Student student = new Student();

        student.setFirstName(registrationDto.getFirstName());
        student.setLastName(registrationDto.getLastName());
        student.setEmail(registrationDto.getEmail());
        student.setUsername(registrationDto.getUsername());
        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(registrationDto.getPassword());
        student.setPassword(encodedPassword);
        student.setCellphoneNumber(registrationDto.getCellphoneNumber());
        // Date
        student.setCreatedAt(registrationDto.getCreatedAt());
        student.setUpdatedAt(registrationDto.getUpdatedAt());

        // Assign ROLE_STUDENT to new registered user
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_STUDENT");
        Role userRole;
        if (optionalRole.isPresent()) {
            userRole = optionalRole.get();
        } else {
            // Create the role if it doesn't exist
            userRole = new Role("ROLE_STUDENT");
            roleRepository.save(userRole);
        }
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        student.setRoles(roles);

        // EXTRA STUDENT INFORMATION
        student.setStudentNo(registrationDto.getStudentNo());
        student.setIdNo(registrationDto.getIdNo());
        student.setImageUrl(registrationDto.getImageUrl());

        // Save the new user to the database
        userRepository.save(student);
        createAdmin();
    }

    public Admin createAdmin(){

        String adminEmail = "admin@gmail.com";
        Admin a = Admin.builder().adminNo("12345673454").build();
        if (!userRepository.existsByEmail(adminEmail)){

            a.setFirstName("Karabo");
            a.setLastName("Pheko");
            a.setUsername("Admin");
            a.setCreatedAt(new Date());
            a.setUpdatedAt(new Date());
            a.setPassword("admin12345");
            a.setCellphoneNumber("0118475934");

            // Assign ROLE_ADMIN to the admin user
            Optional<Role> optionalRole = roleRepository.findByName("ROLE_ADMIN");
            Role adminRole;
            if (optionalRole.isPresent()) {
                adminRole = optionalRole.get();
            } else {
                // Create the role if it doesn't exist
                adminRole = new Role("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            a.setRoles(roles);

            userRepository.save(a);

        }
        return a;
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
