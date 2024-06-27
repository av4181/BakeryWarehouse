package org.example.clientapplication.controllers;

import org.example.clientapplication.dtos.UserAccountDTO;
import org.example.clientapplication.entities.UserAccount;
import org.example.clientapplication.mappers.UserAccountMapper;
import org.example.clientapplication.services.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final UserAccountMapper userAccountMapper;

    private UserAccountController(UserAccountService userAccountService, UserAccountMapper userAccountMapper) {
        this.userAccountService = userAccountService;
        this.userAccountMapper = userAccountMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody UserAccountDTO userAccountDTO) {
        try {
            userAccountService.createUserAccount(userAccountDTO);
            return ResponseEntity.ok("Account created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        try {
            userAccountService.deleteUserAccount(id);
            return ResponseEntity.ok("Account deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserAccountDTO>> getAllAccounts() {
        try {
            List<UserAccount> list = userAccountService.getAllUserAccounts();
            return ResponseEntity.ok(list.stream().map(userAccountMapper::toDTO).collect(Collectors.toList()));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
