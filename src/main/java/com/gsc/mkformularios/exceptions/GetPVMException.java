package com.gsc.mkformularios.exceptions;

public class GetPVMException extends RuntimeException{

    public GetPVMException(String s) {
        super(s);
    }

    public GetPVMException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
