package com.example.ms.sample.photoappuserservice.security;


import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private AuthorizationPropertyHolder authorizationPropertyHolder;

    public AuthorizationFilter(AuthenticationManager authenticationManager, AuthorizationPropertyHolder authorizationPropertyHolder) {
        super(authenticationManager);
        this.authorizationPropertyHolder = authorizationPropertyHolder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authorizationHeader = request.getHeader(authorizationPropertyHolder.getAuthorizationTokenHeaderName());
        if(authorizationHeader == null | !authorizationHeader.startsWith(authorizationPropertyHolder.getAuthorizationTokenHeaderPrefix())){
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken token = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(authorizationPropertyHolder.getAuthorizationTokenHeaderName());
        if(null!=authorizationHeader) {
            String token = authorizationHeader.replace(authorizationPropertyHolder.getAuthorizationTokenHeaderPrefix(), "");
            String userId = Jwts.parser()
                    .setSigningKey(authorizationPropertyHolder.getTokenSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            if(null!=userId) {
                return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
            }
        }
        return null;
    }

}
