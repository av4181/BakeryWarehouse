package org.example.clientapplication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.clientapplication.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Setter
    private UserAccount user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter
    private List<OrderItem> items;
    @Setter
    private LocalDate orderDate;
    @Setter
    private OrderStatus status;
    @Getter
    @Setter
    private double total;
    @Setter
    @Getter
    private double discount;
    @Setter
    @Getter
    private double priceAfterDiscount;
    @Getter
    @Setter
    private double discountPercentage;

    public Long getUserId() {
        return user.getId();
    }
}
