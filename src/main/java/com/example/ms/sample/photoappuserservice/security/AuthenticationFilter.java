package com.example.ms.sample.photoappuserservice.security;

import com.example.ms.sample.photoappuserservice.dto.UserDTO;
import com.example.ms.sample.photoappuserservice.model.LoginRequestModel;
import com.example.ms.sample.photoappuserservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private JwtTokenDataHolder jwtTokenDataHolder;

    public AuthenticationFilter(UserService userService, AuthenticationManager authenticationManager, JwtTokenDataHolder jwtTokenDataHolder){
        this.userService = userService;
        this.jwtTokenDataHolder = jwtTokenDataHolder;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestModel credential = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credential.getEmail(),
                            credential.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        final String userName =((User)authResult.getPrincipal()).getUsername();
        final UserDTO userDTO =  userService.findUserDetailsByEmail(userName);
        final String jwtToken = Jwts.builder()
                .setSubject(userDTO.getUserId())
                .setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(jwtTokenDataHolder.getTokenExpiry())))
                .signWith(SignatureAlgorithm.HS512,jwtTokenDataHolder.getTokenSecret())
                .compact();
        response.addHeader("jwt-token", jwtToken);
        response.addHeader("userId", userDTO.getUserId());



    }
}


