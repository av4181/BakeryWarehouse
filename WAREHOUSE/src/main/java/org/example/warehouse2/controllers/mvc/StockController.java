package org.example.warehouse2.controllers.mvc;

import org.example.warehouse2.controllers.api.dto.IngredientDTO;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.services.IngredientService;
import org.example.warehouse2.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.util.List;

// US-33 Als magazijnier wil ik de stock controleren
@Controller
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;
    private final IngredientService ingredientService;

    @Autowired
    public StockController(StockService stockService, IngredientService ingredientService) {
        this.stockService = stockService;
        this.ingredientService = ingredientService;
    }
    //TODO: stockCount validatie < 0, update van Stock tonen in message informatief
    @GetMapping
    public String showStockList(Model model) {
        List<IngredientDTO> ingredients = ingredientService.getIngredients();
        model.addAttribute("ingredients", ingredients);
        return "stock/stock_list";
    }

    @GetMapping("/low")
    public String showLowStockItems(Model model) {
        List<IngredientDTO> lowStockIngredients = ingredientService.getLowStockIngredients();
        model.addAttribute("ingredients", lowStockIngredients);
        model.addAttribute("lowStock", true);
        return "stock/stock_list";
    }

    @GetMapping("/edit/{ingredientNumber}")
    public String showUpdateStockForm(@PathVariable String ingredientNumber, Model model) {
        try {
            Ingredient ingredient = ingredientService.getIngredientByNumber(ingredientNumber);
            model.addAttribute("ingredient", ingredient);
            return "stock/stock_update";
        } catch (IngredientNotFoundException e) {
            model.addAttribute("error", "Ingredient not found");
            return "error_page";
        }
    }

    @PostMapping("/update/{ingredientNumber}")
    public String updateStock(
            @PathVariable String ingredientNumber,
            @RequestParam("newStockCount") int newStockCount,
            RedirectAttributes redirectAttributes)
    {
        if (newStockCount < 0) {
            redirectAttributes.addFlashAttribute("error", "Stock count cannot be negative.");
            return "redirect:/stock/edit/" + ingredientNumber;
        }

        String updateResult = stockService.updateStock(ingredientNumber, newStockCount) ;
        redirectAttributes.addFlashAttribute("message", updateResult);
        return "redirect:/stock";
    }
}
