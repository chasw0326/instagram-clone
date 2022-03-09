package com.example.instagram2.exception.myException;

import javax.naming.NoPermissionException;

/**
 * 권한 없을때 예외
 */
public class NoAuthorityException extends NoPermissionException {
    public NoAuthorityException(){ }
    public NoAuthorityException(String msg){
        super(msg);
    }
}
