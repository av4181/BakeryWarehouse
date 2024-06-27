package org.example.clientapplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.clientapplication.enums.OrderStatus;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {
    private Long orderId;
    private LocalDate orderDate;
    private double total;
    private OrderStatus status;
}
