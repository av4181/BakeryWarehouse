package org.example.warehouse2.persistence.repositories;

import org.example.warehouse2.model.Delivery;
import org.example.warehouse2.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByStatus(DeliveryStatus status);
    List<Delivery> findByOrderNumber(UUID orderNumber);
    List<Delivery> findByDeliveryDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    Optional<Delivery> findById(Long id);

    // Queries

//    @Query("SELECT d FROM Delivery d JOIN FETCH d.items WHERE d.id = ?1")
//    Optional<Delivery> findByIdWithItems(Long id);
//
//    @Query("SELECT d FROM Delivery d WHERE d.status = 'FAILED' AND d.deliveryDate >= :startDate AND d.deliveryDate <= :endDate")
//    List<Delivery> findFailedDeliveriesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//

}

