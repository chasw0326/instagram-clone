package com.example.instagram2.exception;


public class IllegalFileException extends IllegalArgumentException{
    public IllegalFileException(){ }
    public IllegalFileException(String msg){
        super(msg);
    }
}
