package org.example.bakery2.repositories;

import org.example.bakery2.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoringCase(String productName);

    Optional<Product> findByUuid(UUID uuid);
}
