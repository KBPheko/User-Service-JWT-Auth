package com.simplysave.userservice.UserService.security;

public class JwtAuthResponse {

    private String token;

    public JwtAuthResponse() {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAccessToken(String token) {
        this.token = token;
    }

}
