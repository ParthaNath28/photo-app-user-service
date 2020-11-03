package com.example.ms.sample.photoappuserservice.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class JwtTokenDataHolder {
    private String tokenExpiry;
    private String tokenSecret;
}

