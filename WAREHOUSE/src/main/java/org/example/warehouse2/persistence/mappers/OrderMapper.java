package org.example.warehouse2.persistence.mappers;

import org.example.warehouse2.controllers.api.dto.NewOrderDTO;
import org.example.warehouse2.controllers.api.dto.OrderItemDTO;
import org.example.warehouse2.controllers.api.dto.OrderDTO;
import org.example.warehouse2.model.Order;
import org.example.warehouse2.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    //    @Mapping(target = "items", source = "items")
    OrderDTO toDTO(Order order);
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Order toEntity(NewOrderDTO orderDTO);
    @Mapping(target = "order", ignore = true)
    OrderItem toEntity(OrderItemDTO orderItemDTO);
}

