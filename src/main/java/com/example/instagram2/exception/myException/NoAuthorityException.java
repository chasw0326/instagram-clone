package com.example.instagram2.exception.myException;

import javax.naming.NoPermissionException;

public class NoAuthorityException extends NoPermissionException {
    public NoAuthorityException(){ }
    public NoAuthorityException(String msg){
        super(msg);
    }
}
