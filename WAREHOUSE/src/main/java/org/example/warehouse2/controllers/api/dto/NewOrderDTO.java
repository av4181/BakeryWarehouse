package org.example.warehouse2.controllers.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderDTO {
    @NotNull(message = "Customer number is mandatory")
    private String customerNumber;

    @NotEmpty(message = "Order items are mandatory")
    private List<OrderItemDTO> items;
}
