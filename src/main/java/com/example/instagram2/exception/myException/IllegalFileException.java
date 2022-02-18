package com.example.instagram2.exception.myException;


public class IllegalFileException extends IllegalArgumentException{
    public IllegalFileException(){ }
    public IllegalFileException(String msg){
        super(msg);
    }
}
