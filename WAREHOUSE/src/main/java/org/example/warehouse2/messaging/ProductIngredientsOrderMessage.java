package org.example.warehouse2.messaging;

import lombok.*;
import org.example.warehouse2.model.DeliveryStatus;
import org.example.warehouse2.model.OrderItem;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

// US-49 Bakkerij moet bevestiging krijgen via Warehouse dat een bestelling door warehouse zal/wordt geleverd
@Data
public class ProductIngredientsOrderMessage implements Serializable {
    private UUID orderId;
    private List<OrderItem> items;

    public ProductIngredientsOrderMessage(UUID orderId, List<OrderItem> items) {
        this.orderId = orderId;
        this.items = items;
    }
}
