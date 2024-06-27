package org.example.clientapplication.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.clientapplication.entities.LoyaltyLevel;
import org.example.clientapplication.entities.UserAccount;

@Data
@Builder
@Setter
@Getter
public class LoyaltyInfoDto {
    private Long id;
    private Long userId;
    private int loyaltyPoints;
    private LoyaltyLevel level;
}

