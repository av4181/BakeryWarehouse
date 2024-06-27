package org.example.warehouse2.persistence.repositories;

import jakarta.transaction.Transactional;
import org.example.warehouse2.model.Supplier;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByName(String name);

    List<Supplier> findByContactPerson(String contactPerson);
    List<Supplier> findByNameContainingIgnoreCaseAndContactPersonContainingIgnoreCase(String name, String contactPerson);
    boolean existsById(Long id);
    @Query("SELECT s FROM Supplier s WHERE s.name LIKE %:name%")
    List<Supplier> searchByName(@Param("name") String name);
}
