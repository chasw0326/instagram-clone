package com.example.instagram2.exception.myException;

public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(){ }
    public InvalidPasswordException(String msg){
        super(msg);
    }
}
