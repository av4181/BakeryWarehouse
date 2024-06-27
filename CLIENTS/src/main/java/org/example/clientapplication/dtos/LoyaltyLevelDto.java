package org.example.clientapplication.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class LoyaltyLevelDto {

    private String level;
    private int points;
    private double discountPercentage;
}
