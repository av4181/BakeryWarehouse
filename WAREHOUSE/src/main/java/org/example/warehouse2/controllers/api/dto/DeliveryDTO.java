package org.example.warehouse2.controllers.api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.warehouse2.model.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
    private Long id;
    private UUID orderNumber;
    private String destination;
    private LocalDateTime deliveryDate;
    private DeliveryStatus status;
}
