package org.example.clientapplication.mappers;

import org.example.clientapplication.dtos.LoyaltyInfoDto;
import org.example.clientapplication.entities.LoyaltyInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface LoyaltyInfoMapper {
    @Mapping(target = "userId", source = "user.id")
    LoyaltyInfoDto toDto(LoyaltyInfo loyaltyInfo);

    @Mapping(target = "user.id", source = "userId")
    LoyaltyInfo toEntity(LoyaltyInfoDto loyaltyInfoDto);
}
