package com.simplysave.userservice.UserService.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Karabo Pheko
 * */

/**
 * AuthenticationEntryPoint is used by ExceptionTranslationFilter to commence an authentication scheme
 * It is the entry point to check if a user is authenticated and logs the person in or throws an exception (unauthorized).
 * Usually, the class can be used like that in simple applications but when using Spring security in REST, JWT, etc one
 * will have to extend it to provide better Spring Security filter chain management.
 * */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

    }
}
