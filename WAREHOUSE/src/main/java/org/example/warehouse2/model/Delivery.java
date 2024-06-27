package org.example.warehouse2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Expliciet @Column annotatie en db kolomnaam anders mapping problemen.

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Delivery implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column (name = "ordernumber")
    private UUID orderNumber;
    @Column
    private String destination;
    @Column (name = "deliverydate")
    private LocalDateTime deliveryDate;
    @Column
    private DeliveryStatus status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "delivery")
    private List<DeliveryItem> items = new ArrayList<>();
}
