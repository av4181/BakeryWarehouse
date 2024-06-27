package org.example.warehouse2.messaging;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class InventoryUpdateMessage implements Serializable {
    private String ingredientNumber;
    private int newStockLevel;

    public InventoryUpdateMessage(String ingredientNumber, int newStockLevel) {
        this.ingredientNumber = ingredientNumber;
        this.newStockLevel = newStockLevel;
    }

    // TODO
}
