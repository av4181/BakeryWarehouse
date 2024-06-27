package org.example.clientapplication.repositories;

import org.example.clientapplication.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long>{

}
