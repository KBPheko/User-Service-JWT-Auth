package com.simplysave.userservice.UserService.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RegistrationDto {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String cellphoneNumber;
    private String password;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();
}
