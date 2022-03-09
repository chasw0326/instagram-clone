package com.example.instagram2.exception.myException;

/**
 * 비밀번호 틀릴때 예외
 */
public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(){ }
    public InvalidPasswordException(String msg){
        super(msg);
    }
}
