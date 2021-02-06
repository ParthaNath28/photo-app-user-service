package com.example.ms.sample.photoappuserservice.security;

import com.example.ms.sample.photoappuserservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Value("${login.url.path}")
    private String loginUrlPath;

    @Value("${api.registration.url.path}")
    private String registrationUrlPath;

    @Value("${api.h2console.url.path}")
    private String h2ConsoleUrlPath;

    @Value("${api.actuator.url.path}")
    private String actuatorUrlPath;

    @Value("${api.users.actuator.url.path}")
    private String usersServiceActuatorUrlPath;

    @Value("${api.gateway.ip}")
    private String apiGatewayIp;

    @Autowired
    private UserService userService;

    @Value("${token.expiry.value}")
    private String tokenExpiry;

    @Value("${token.secret.value}")
    private String tokenSecret;

    @Value("${authorization.token.header.name}")
    private String authorizationTokenHeaderName;

    @Value("${authorization.token.header.prefix}")
    private String authorizationTokenHeaderPrefix;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        AuthorizationPropertyHolder authorizationPropertyHolder = AuthorizationPropertyHolder.builder()
                .authorizationTokenHeaderName(authorizationTokenHeaderName)
                .authorizationTokenHeaderPrefix(authorizationTokenHeaderPrefix)
                .tokenSecret(tokenSecret).build();
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthorizationFilter(authenticationManager(), authorizationPropertyHolder))
                .addFilter(getAuthenticationFiler())
        ;
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }


    private AuthenticationFilter getAuthenticationFiler() throws Exception{
        JwtTokenDataHolder jwtTokenDataHolder = getJWTTokenHolder();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService,authenticationManager(),jwtTokenDataHolder);
        authenticationFilter.setFilterProcessesUrl(loginUrlPath);
        return  authenticationFilter;
    }

    private JwtTokenDataHolder getJWTTokenHolder() {
        return JwtTokenDataHolder.builder()
                .tokenExpiry(tokenExpiry)
                .tokenSecret(tokenSecret).build();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity webSecurity){
        webSecurity
                .ignoring()
                .antMatchers(h2ConsoleUrlPath)
                .antMatchers(actuatorUrlPath)
                .antMatchers(usersServiceActuatorUrlPath)
                .antMatchers(HttpMethod.POST, registrationUrlPath);

    }

}


