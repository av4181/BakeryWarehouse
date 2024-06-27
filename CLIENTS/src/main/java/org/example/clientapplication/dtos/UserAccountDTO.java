package org.example.clientapplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.clientapplication.enums.UserType;

@Getter
@Setter
@AllArgsConstructor
public class UserAccountDTO {

    private String username;
    private String password;
    private String email;
    private String type;
}
