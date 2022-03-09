package com.example.instagram2.exception.myException;

import org.springframework.dao.DuplicateKeyException;

/**
 * 중복예외
 */
public class DuplicationException extends DuplicateKeyException {
    public DuplicationException(){
        super("DuplicationException: ");
    }
    public DuplicationException(String msg){
        super(msg);
    }
}
