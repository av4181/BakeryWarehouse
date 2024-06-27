package org.example.clientapplication.controllers;

import org.example.clientapplication.dtos.LoyaltyInfoDto;
import org.example.clientapplication.entities.LoyaltyInfo;
import org.example.clientapplication.exceptions.UserNotFoundException;
import org.example.clientapplication.mappers.LoyaltyInfoMapper;
import org.example.clientapplication.services.LoyaltyInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loyalty")
public class LoyaltyInfoController {

    private final LoyaltyInfoService loyaltyInfoService;
    private final LoyaltyInfoMapper loyaltyInfoMapper;

    public LoyaltyInfoController(LoyaltyInfoService loyaltyInfoService, LoyaltyInfoMapper loyaltyInfoMapper) {
        this.loyaltyInfoService = loyaltyInfoService;
        this.loyaltyInfoMapper = loyaltyInfoMapper;
    }

    @GetMapping("/info/{userId}")
    public LoyaltyInfoDto getLoyaltyInfo(@PathVariable Long userId) {
        LoyaltyInfo loyaltyInfo = loyaltyInfoService.getLoyaltyInfo(userId);
        if(loyaltyInfo == null) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        return loyaltyInfoMapper.toDto(loyaltyInfoService.getLoyaltyInfo(userId));
    }

    @GetMapping("info/allUsers")
    public List<LoyaltyInfoDto> getAllLoyaltyInfo() {
        List<LoyaltyInfo> list = loyaltyInfoService.getAllLoyaltyInfo();
        return list.stream().map(loyaltyInfoMapper::toDto).toList();
    }
}
