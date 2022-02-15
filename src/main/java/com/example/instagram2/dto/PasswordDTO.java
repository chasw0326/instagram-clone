package com.example.instagram2.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class PasswordDTO {

    Long mno;
    String imgUrl;
    String username;

    @NotEmpty
    String oldPw;
    String newPw;
    String checkNewPw;

}
