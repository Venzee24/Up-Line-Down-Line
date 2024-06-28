package com.cyberz.ar7demon.dto.auth;

import lombok.Data;

@Data
public class SignUpDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;

}
