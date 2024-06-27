package org.example.clientapplication.serviceTests;

import org.example.clientapplication.dtos.UserAccountDTO;
import org.example.clientapplication.entities.LoyaltyInfo;
import org.example.clientapplication.entities.UserAccount;
import org.example.clientapplication.enums.UserType;
import org.example.clientapplication.repositories.UserAccountRepository;
import org.example.clientapplication.services.LoyaltyInfoService;
import org.example.clientapplication.services.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTests {

    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private LoyaltyInfoService loyaltyInfoService;

    @InjectMocks
    private UserAccountService userAccountService;

    private UserAccount userAccount;
    private UserAccountDTO userAccountDTO;

    @BeforeEach
    void setUp(){
        userAccountDTO = new UserAccountDTO("username","password","email","B2C");
        userAccount = new UserAccount();
        userAccount.setUsername(userAccountDTO.getUsername());
        userAccount.setPassword(userAccountDTO.getPassword());
        userAccount.setEmail(userAccountDTO.getEmail());
        userAccount.setType(UserType.B2C);
    }
    @Test
    void testCreateUserAccount(){
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);
        UserAccount created = userAccountService.createUserAccount(userAccountDTO);

        assertNotNull(created);
        assertEquals(userAccount.getUsername(), created.getUsername());
        verify(loyaltyInfoService,times(1)).createLoyaltyInfo(any(UserAccount.class));
    }

    @Test
    void testDeleteUserAccount(){
        doNothing().when(loyaltyInfoService).deleteLoyaltyInfo(anyLong());
        doNothing().when(userAccountRepository).deleteById(anyLong());

        userAccountService.deleteUserAccount(1L);

        verify(loyaltyInfoService,times(1)).deleteLoyaltyInfo(anyLong());
        verify(userAccountRepository,times(1)).deleteById(anyLong());
    }

    @Test
    void testGetAllUserAccounts(){
        when(userAccountRepository.findAll()).thenReturn(List.of(userAccount));

        assertEquals(1,userAccountService.getAllUserAccounts().size());
        verify(userAccountRepository,times(1)).findAll();
    }

}
