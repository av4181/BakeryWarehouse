package org.example.clientapplication.mappers;

import org.example.clientapplication.dtos.LoyaltyLevelDto;
import org.example.clientapplication.entities.LoyaltyLevel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoyaltyLevelMapper {

    LoyaltyLevel toEntity(LoyaltyLevelDto loyaltyLevelDto);

    LoyaltyLevelDto toDto(LoyaltyLevel loyaltyLevel);
}
