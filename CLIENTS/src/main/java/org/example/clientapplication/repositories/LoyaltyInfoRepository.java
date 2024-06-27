package org.example.clientapplication.repositories;

import org.example.clientapplication.entities.LoyaltyInfo;
import org.example.clientapplication.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyInfoRepository extends JpaRepository<LoyaltyInfo, Long> {
    LoyaltyInfo findByUser(UserAccount user);

    LoyaltyInfo findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
