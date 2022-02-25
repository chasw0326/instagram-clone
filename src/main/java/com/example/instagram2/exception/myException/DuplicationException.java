package com.example.instagram2.exception.myException;

import org.springframework.dao.DuplicateKeyException;

public class DuplicationException extends DuplicateKeyException {
    public DuplicationException(){
        super("DuplicationException: ");
    }
    public DuplicationException(String msg){
        super(msg);
    }
}
