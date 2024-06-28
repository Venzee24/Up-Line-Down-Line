package com.cyberz.ar7demon.dto.auth;

import lombok.Data;

@Data
public class SignInResponseDto {
    private String token;
    private String refreshedToken;
}
