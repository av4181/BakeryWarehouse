package org.example.clientapplication.repositories;

import org.example.clientapplication.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductNumber(UUID productNumber);

    List<Product> findByPriceIsNull();
}
