package org.example.clientapplication.controllers;

import org.example.clientapplication.dtos.LoyaltyLevelDto;
import org.example.clientapplication.entities.LoyaltyLevel;
import org.example.clientapplication.exceptions.LoyaltyLevelNotFoundException;
import org.example.clientapplication.mappers.LoyaltyLevelMapper;
import org.example.clientapplication.services.LoyaltyLevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loyalty")
public class LoyaltyLevelController {
    private final LoyaltyLevelService loyaltyLevelService;

    private final LoyaltyLevelMapper loyaltyLevelMapper;

    public LoyaltyLevelController(LoyaltyLevelService loyaltyLevelService, LoyaltyLevelMapper loyaltyLevelMapper) {
        this.loyaltyLevelService = loyaltyLevelService;
        this.loyaltyLevelMapper = loyaltyLevelMapper;
    }

    @GetMapping
    public List<LoyaltyLevelDto> getLoyaltyLevels() {
        return loyaltyLevelService.getLoyaltyLevels().stream()
                .map(loyaltyLevelMapper::toDto)
                .toList();
    }

    @GetMapping("/{level}")
    public ResponseEntity<LoyaltyLevelDto> getLoyaltyLevel(@PathVariable String level) {
        LoyaltyLevel loyaltyLevel = loyaltyLevelService.getLoyaltyLevelByLevel(level);
        if (loyaltyLevel == null) {
            throw new LoyaltyLevelNotFoundException("Loyalty level not found for level: " + level);
        }
        return ResponseEntity.ok(loyaltyLevelMapper.toDto(loyaltyLevel));
    }


    @PatchMapping("/{level}")
    public ResponseEntity<LoyaltyLevelDto> updateLoyaltyLevel(@PathVariable String level, @RequestBody LoyaltyLevel updatedLoyaltyLevel) {
        LoyaltyLevel existingLoyaltyLevel = loyaltyLevelService.getLoyaltyLevelByLevel(level.toUpperCase());
        if (existingLoyaltyLevel == null) {
            throw new LoyaltyLevelNotFoundException("Loyalty level not found for level: " + level);
        }
        if (updatedLoyaltyLevel.getPoints() != 0) {
            existingLoyaltyLevel.setPoints(updatedLoyaltyLevel.getPoints());
        }
        if (updatedLoyaltyLevel.getDiscountPercentage() != 0) {
            existingLoyaltyLevel.setDiscountPercentage(updatedLoyaltyLevel.getDiscountPercentage());
        }
        LoyaltyLevel updatedLevel = loyaltyLevelService.updateLoyaltyLevel(existingLoyaltyLevel);
        return ResponseEntity.ok(loyaltyLevelMapper.toDto(updatedLevel));
    }
}

