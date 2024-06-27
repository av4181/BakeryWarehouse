package org.example.clientapplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.clientapplication.entities.Order;
import org.example.clientapplication.entities.OrderItem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long UserId;
    private List<OrderItemDTO> items;
    private LocalDate orderDate;

    public void copyFrom(Order exisitingOrder) {
        this.UserId = exisitingOrder.getUserId();
        this.items = toOrderItemDTOList(exisitingOrder.getItems());
        this.orderDate = exisitingOrder.getOrderDate();
    }

    public List<OrderItemDTO> toOrderItemDTOList(List<OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.copyFrom(orderItem);
            return orderItemDTO;
        }).collect(Collectors.toList());
    }
}
