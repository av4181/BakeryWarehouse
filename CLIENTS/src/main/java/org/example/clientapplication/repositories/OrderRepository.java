package org.example.clientapplication.repositories;

import org.example.clientapplication.entities.Order;
import org.example.clientapplication.entities.UserAccount;
import org.example.clientapplication.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(UserAccount user);

    List<Order> findByUserAndStatusAndOrderDateBetween(UserAccount user, OrderStatus status, LocalDate startDate, LocalDate endDate);

    List<Order> findByUserAndStatus(UserAccount user, OrderStatus status);

    List<Order> findByUserAndOrderDateBetween(UserAccount user, LocalDate startDate, LocalDate endDate);
}
