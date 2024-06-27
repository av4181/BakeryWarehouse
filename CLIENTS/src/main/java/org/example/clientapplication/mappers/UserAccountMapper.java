package org.example.clientapplication.mappers;

import org.example.clientapplication.dtos.UserAccountDTO;
import org.example.clientapplication.entities.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    UserAccount toEntity(UserAccountDTO userAccountDTO);

    UserAccountDTO toDTO(UserAccount userAccount);
}
