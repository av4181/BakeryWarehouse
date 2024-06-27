package org.example.clientapplication.services;

import jakarta.transaction.Transactional;
import org.example.clientapplication.dtos.UserAccountDTO;
import org.example.clientapplication.entities.UserAccount;
import org.example.clientapplication.enums.UserType;
import org.example.clientapplication.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private LoyaltyInfoService loyaltyInfoService;

    public UserAccount createUserAccount(UserAccountDTO userAccountDTO) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(userAccountDTO.getUsername());
        userAccount.setPassword(userAccountDTO.getPassword());
        userAccount.setEmail(userAccountDTO.getEmail());
        userAccount.setType(UserType.valueOf(userAccountDTO.getType()));
        loyaltyInfoService.createLoyaltyInfo(userAccount);
        return userAccountRepository.save(userAccount);
    }

    public void deleteUserAccount(Long id) {
        loyaltyInfoService.deleteLoyaltyInfo(id);
        userAccountRepository.deleteById(id);
    }

    public List<UserAccount> getAllUserAccounts() {
        return userAccountRepository.findAll();
    }
}
