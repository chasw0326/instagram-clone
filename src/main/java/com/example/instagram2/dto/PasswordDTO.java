package com.example.instagram2.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordDTO {

    Long mno;
    String imgUrl;
    String username;
    String oldPw;
    String newPw;
    String checkNewPw;

}
