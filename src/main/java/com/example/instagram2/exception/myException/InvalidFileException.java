package com.example.instagram2.exception.myException;


public class InvalidFileException extends IllegalArgumentException{
    public InvalidFileException(){ }
    public InvalidFileException(String msg){
        super(msg);
    }
}
