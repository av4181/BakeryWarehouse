package org.example.warehouse2.controllers.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.warehouse2.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private UUID id;
    private LocalDateTime orderDate;
    private OrderStatus status;
//    private List<OrderItemDTO> items;
}
