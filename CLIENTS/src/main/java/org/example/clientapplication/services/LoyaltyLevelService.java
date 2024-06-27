package org.example.clientapplication.services;

import jakarta.transaction.Transactional;
import org.example.clientapplication.entities.LoyaltyLevel;
import org.example.clientapplication.repositories.LoyaltyLevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LoyaltyLevelService {

    private final LoyaltyLevelRepository loyaltyLevelRepository;

    public LoyaltyLevelService(LoyaltyLevelRepository loyaltyLevelRepository) {
        this.loyaltyLevelRepository = loyaltyLevelRepository;
    }
    public List<LoyaltyLevel> getLoyaltyLevels() {
        return loyaltyLevelRepository.findAll();
    }

    public LoyaltyLevel getLoyaltyLevelByLevel(String level) {
        return loyaltyLevelRepository.findByLevel(level);
    }

    public LoyaltyLevel updateLoyaltyLevel(LoyaltyLevel level) {
        return loyaltyLevelRepository.save(level);
    }
}
