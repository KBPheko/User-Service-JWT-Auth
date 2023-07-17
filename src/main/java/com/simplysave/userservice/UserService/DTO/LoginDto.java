package com.simplysave.userservice.UserService.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    private String usernameOrEmail;
    private String password;

}
