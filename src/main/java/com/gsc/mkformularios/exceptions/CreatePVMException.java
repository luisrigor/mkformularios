package com.gsc.mkformularios.exceptions;

public class CreatePVMException extends RuntimeException {

    public CreatePVMException(String s) {
        super(s);
    }

    public CreatePVMException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
