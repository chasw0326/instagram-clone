package com.example.instagram2.exception.myException;

/**
 * 파일예외
 */
public class InvalidFileException extends IllegalArgumentException{
    public InvalidFileException(){ }
    public InvalidFileException(String msg){
        super(msg);
    }
}
