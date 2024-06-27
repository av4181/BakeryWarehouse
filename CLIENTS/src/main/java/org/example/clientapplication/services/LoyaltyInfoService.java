package org.example.clientapplication.services;

import jakarta.transaction.Transactional;
import org.example.clientapplication.dtos.LoyaltyInfoDto;
import org.example.clientapplication.entities.LoyaltyInfo;
import org.example.clientapplication.entities.UserAccount;
import org.example.clientapplication.mappers.LoyaltyInfoMapper;
import org.example.clientapplication.repositories.LoyaltyInfoRepository;
import org.example.clientapplication.repositories.LoyaltyLevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LoyaltyInfoService {

    private final LoyaltyInfoRepository loyaltyInfoRepository;

    private final LoyaltyLevelRepository loyaltyLevelRepository;

    private final LoyaltyInfoMapper loyaltyInfoMapper;

    public LoyaltyInfoService(LoyaltyInfoRepository loyaltyInfoRepository, LoyaltyLevelRepository loyaltyLevelRepository, LoyaltyInfoMapper loyaltyInfoMapper) {
        this.loyaltyInfoRepository = loyaltyInfoRepository;
        this.loyaltyLevelRepository = loyaltyLevelRepository;
        this.loyaltyInfoMapper = loyaltyInfoMapper;
    }


    public LoyaltyInfo getLoyaltyInfo(Long userId) {
        return loyaltyInfoRepository.findByUserId(userId);
    }

    public void updateLoyaltyPoints(Long userId, int newPoints) {
        LoyaltyInfo loyaltyInfo = loyaltyInfoRepository.findByUserId(userId);
        loyaltyInfo.setLoyaltyPoints(newPoints);
        loyaltyInfoRepository.save(loyaltyInfo);
        updateLoyaltyLevel(userId);
    }

    private void updateLoyaltyLevel(long userId) {
        LoyaltyInfo loyaltyInfo = loyaltyInfoRepository.findByUserId(userId);
        int points = loyaltyInfo.getLoyaltyPoints();
        if (points < 1000) {
            loyaltyInfo.setLevel(loyaltyLevelRepository.findByLevel("BRONZE"));
        } else if (points < 5000) {
            loyaltyInfo.setLevel(loyaltyLevelRepository.findByLevel("SILVER"));
        } else if (points <= 10000) {
            loyaltyInfo.setLevel(loyaltyLevelRepository.findByLevel("GOLD"));
        } else {
            loyaltyInfo.setLevel(loyaltyLevelRepository.findByLevel("PLATINUM"));
        }
        loyaltyInfoRepository.save(loyaltyInfo);
    }

    public void createLoyaltyInfo(UserAccount user) {
        LoyaltyInfo loyaltyInfo = new LoyaltyInfo();
        loyaltyInfo.setUser(user);
        loyaltyInfo.setLoyaltyPoints(0);
        loyaltyInfo.setLevel(loyaltyLevelRepository.findByLevel("BRONZE"));
        loyaltyInfoRepository.save(loyaltyInfo);
    }

    @Transactional
    public void deleteLoyaltyInfo(Long id) {
        loyaltyInfoRepository.deleteByUserId(id);
    }

    public List<LoyaltyInfo> getAllLoyaltyInfo() {
        return loyaltyInfoRepository.findAll();
    }
}
