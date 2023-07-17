package com.simplysave.userservice.UserService.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * @author Karabo Pheko
 * */

/**
 * Utility class
 * JwtTokenProvider which provides methods for generating,
 * validating, and extracting information from JSON Web
 * Tokens (JWTs) used for authentication in a Spring Boot
 * application.
 * */

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    /***
     * The method generates a new JWT
     * based on the provided Authentication object
     * which contains information about the user being authenticated.
     * It uses the Jwts.builder() method to create a new JwtBuilder object,
     * sets the subject (i.e., username) of the JWT,the issue date, and expiration date,
     * and signs the JWT using the key() method.
     * Finally, it returns the JWT as a string
     * */

    // generate JWT token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    /**
     *
     * getUsername(String token) method extracts the username from the provided JWT
     *  It uses the Jwts.parserBuilder() method to create a new JwtParserBuilder object,
     *  sets the signing key using the key() method and parses the JWT using the parseClaimsJws() method.
     *   It then retrieves the subject (i.e., username) from the JWT's Claims object and returns it as a string.
     *
     * */

    // get username from Jwt token
    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }

    /**
     *
     * validateToken(String token) method validates the provided JWT.
     * It uses the Jwts.parserBuilder() method to create a new JwtParserBuilder object,
     * sets the signing key using the key() method and parses the JWT using the parse() method.
     * If the JWT is valid, the method returns true.
     * If the JWT is invalid or has expired, the method logs an error message using the logger
     * object and returns false.
     *
     * */

    // validate Jwt token
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
