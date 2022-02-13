package com.example.instagram2.dto;


import com.example.instagram2.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUpdateReqDTO {

    private Long mno;
    private String name;
    private String username;
    private String website;
    private String intro;
    private String email;
    private String phone;
    private String gender;

}
