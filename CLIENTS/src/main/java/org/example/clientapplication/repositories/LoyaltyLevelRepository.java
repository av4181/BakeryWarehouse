package org.example.clientapplication.repositories;

import org.example.clientapplication.entities.LoyaltyLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyLevelRepository extends JpaRepository<LoyaltyLevel, Long> {

    LoyaltyLevel findByLevel(String level);

}
