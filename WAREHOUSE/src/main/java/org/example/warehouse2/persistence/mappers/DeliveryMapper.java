package org.example.warehouse2.persistence.mappers;

import org.example.warehouse2.controllers.api.dto.DeliveryDTO;
import org.example.warehouse2.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    //    @Mapping(target = "items", source = "items")
    DeliveryDTO toDTO(Delivery delivery);

    Delivery toEntity(DeliveryDTO deliveryDTO);
}

