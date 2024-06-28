package com.cyberz.ar7demon.exception;

import java.security.NoSuchAlgorithmException;

public class AuthenticationFailException extends RuntimeException {
    public AuthenticationFailException(String message){
        super(message);
    }
}
