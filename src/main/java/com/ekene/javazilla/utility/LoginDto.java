package com.ekene.javazilla.utility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String email;
    private String passWord;
    private Long id;
}
