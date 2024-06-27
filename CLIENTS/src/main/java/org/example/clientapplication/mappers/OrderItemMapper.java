package org.example.clientapplication.mappers;

import org.example.clientapplication.dtos.OrderItemDTO;
import org.example.clientapplication.entities.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemDTO toDto(OrderItem orderItem);

    OrderItem toEntity(OrderItemDTO orderItemDTO);
}
