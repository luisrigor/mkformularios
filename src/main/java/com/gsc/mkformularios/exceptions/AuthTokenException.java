package com.gsc.mkformularios.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthTokenException extends AuthenticationException {

    public AuthTokenException(String msg) {
        super(msg);
    }

    public AuthTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
