package org.example.clientapplication.serviceTests;

import org.example.clientapplication.entities.LoyaltyInfo;
import org.example.clientapplication.entities.LoyaltyLevel;
import org.example.clientapplication.entities.UserAccount;
import org.example.clientapplication.mappers.LoyaltyInfoMapper;
import org.example.clientapplication.repositories.LoyaltyInfoRepository;
import org.example.clientapplication.repositories.LoyaltyLevelRepository;
import org.example.clientapplication.services.LoyaltyInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class LoyaltyInfoServiceTests {

    @Mock
    private LoyaltyInfoRepository loyaltyInfoRepository;

    @Mock
    private LoyaltyLevelRepository loyaltyLevelRepository;

    @Mock
    private LoyaltyInfoMapper loyaltyInfoMapper;

    private LoyaltyInfoService loyaltyInfoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        loyaltyInfoService = new LoyaltyInfoService(loyaltyInfoRepository, loyaltyLevelRepository, loyaltyInfoMapper);
    }

    @Test
    void testGetLoyaltyInfo_UserExists() {
        Long userId = 1L;
        LoyaltyInfo expectedInfo = new LoyaltyInfo();
        when(loyaltyInfoRepository.findByUserId(userId)).thenReturn(expectedInfo);

        LoyaltyInfo actualInfo = loyaltyInfoService.getLoyaltyInfo(userId);

        assertEquals(expectedInfo, actualInfo);
    }

    @Test
    void testGetLoyaltyInfo_UserDoesNotExist() {
        Long userId = 1L;
        when(loyaltyInfoRepository.findByUserId(userId)).thenReturn(null);

        LoyaltyInfo actualInfo = loyaltyInfoService.getLoyaltyInfo(userId);

        assertEquals(null, actualInfo);
    }

    @Test
    void testUpdateLoyaltyPoints(){
        Long userId = 1L;
        int newPoints = 1153;
        LoyaltyInfo loyaltyInfo = new LoyaltyInfo();

        when(loyaltyInfoRepository.findByUserId(userId)).thenReturn(loyaltyInfo);

        loyaltyInfoService. updateLoyaltyPoints(userId, newPoints);

        assertEquals(newPoints, loyaltyInfo.getLoyaltyPoints());

        verify(loyaltyInfoRepository, times(2)).save(loyaltyInfo);
    }

    @Test
    void testCreateLoyaltyInfo(){
        UserAccount user = new UserAccount();

        when(loyaltyLevelRepository.findByLevel("BRONZE")).thenReturn(null);

        loyaltyInfoService.createLoyaltyInfo(user);

        verify(loyaltyInfoRepository, times(1)).save(any(LoyaltyInfo.class));
    }

    @Test
    void testDeleteLoyaltyInfo(){
        Long id = 1L;
        doNothing().when(loyaltyInfoRepository).deleteByUserId(id);

        loyaltyInfoService.deleteLoyaltyInfo(id);

        verify(loyaltyInfoRepository, times(1)).deleteByUserId(id);
    }

    @Test
    void testGetAllLoyaltyInfo(){
        loyaltyInfoService.getAllLoyaltyInfo();

        verify(loyaltyInfoRepository, times(1)).findAll();
    }

    @Test
    void testUpdateLoyaltyLevel_BelowBronze(){
        Long userId = 1L;
        LoyaltyInfo loyaltyInfo = new LoyaltyInfo();
        loyaltyInfo.setLoyaltyPoints(500);

        when(loyaltyInfoRepository.findByUserId(userId)).thenReturn(loyaltyInfo);
        when(loyaltyLevelRepository.findByLevel("BRONZE")).thenReturn(LoyaltyLevel.builder().level("BRONZE").build());

        loyaltyInfoService.updateLoyaltyPoints(userId, 500);

        verify(loyaltyInfoRepository, times(2)).save(loyaltyInfo);

        assertEquals("BRONZE", loyaltyInfo.getLevel().getLevel());
    }

    @Test
    void testUpdateLoyaltyLevel_BronzeToSilver(){
        Long userId = 1L;
        LoyaltyInfo loyaltyInfo = new LoyaltyInfo();
        loyaltyInfo.setLoyaltyPoints(2500);

        when(loyaltyInfoRepository.findByUserId(userId)).thenReturn(loyaltyInfo);
        when(loyaltyLevelRepository.findByLevel("SILVER")).thenReturn(LoyaltyLevel.builder().level("SILVER").build());

        loyaltyInfoService.updateLoyaltyPoints(userId, 2500);

        verify(loyaltyInfoRepository, times(2)).save(loyaltyInfo);

        assertEquals("SILVER", loyaltyInfo.getLevel().getLevel());
    }

    @Test
    void testUpdateLoyaltyLevel_SilverToGold(){
        Long userId = 1L;
        LoyaltyInfo loyaltyInfo = new LoyaltyInfo();
        loyaltyInfo.setLoyaltyPoints(7500);

        when(loyaltyInfoRepository.findByUserId(userId)).thenReturn(loyaltyInfo);
        when(loyaltyLevelRepository.findByLevel("GOLD")).thenReturn(LoyaltyLevel.builder().level("GOLD").build());

        loyaltyInfoService.updateLoyaltyPoints(userId, 7500);

        verify(loyaltyInfoRepository, times(2)).save(loyaltyInfo);

        assertEquals("GOLD", loyaltyInfo.getLevel().getLevel());
    }

    @Test
    void testUpdateLoyaltyLevel_GoldToPlatinum(){
        Long userId = 1L;
        LoyaltyInfo loyaltyInfo = new LoyaltyInfo();
        loyaltyInfo.setLoyaltyPoints(15000);

        when(loyaltyInfoRepository.findByUserId(userId)).thenReturn(loyaltyInfo);
        when(loyaltyLevelRepository.findByLevel("PLATINUM")).thenReturn(LoyaltyLevel.builder().level("PLATINUM").build());

        loyaltyInfoService.updateLoyaltyPoints(userId, 15000);

        verify(loyaltyInfoRepository, times(2)).save(loyaltyInfo);

        assertEquals("PLATINUM", loyaltyInfo.getLevel().getLevel());
    }
}
