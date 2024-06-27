package org.example.clientapplication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.clientapplication.enums.UserType;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_accounts")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private String email;
    @Setter
    private UserType type;
}
