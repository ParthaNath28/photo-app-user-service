package com.example.ms.sample.photoappuserservice.security;

import com.example.ms.sample.photoappuserservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Value("${api.gateway.ip}")
    private String apiGatewayIp;

    @Autowired
    private UserService userService;

    @Value("${token.expiry.value}")
    private String tokenExpiry;

    @Value("${token.secret.value}")
    private String tokenSecret;

    @Value("${login.url.path}")
    private String loginUrlPath;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .addFilter(getAuthenticationFiler())
                .csrf().disable();
        http.headers().frameOptions().disable();

    }

    private AuthenticationFilter getAuthenticationFiler() throws Exception{
        JwtTokenDataHolder jwtTokenDataHolder = JwtTokenDataHolder.builder()
                .tokenExpiry(tokenExpiry)
                .tokenSecret(tokenSecret).build();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService,authenticationManager(),jwtTokenDataHolder);
        authenticationFilter.setFilterProcessesUrl(loginUrlPath);
        return  authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

}


