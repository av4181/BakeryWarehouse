package org.example.warehouse2.controllerTests;
import org.example.warehouse2.controllers.api.dto.IngredientDTO;
import org.example.warehouse2.controllers.mvc.StockController;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.services.IngredientService;
import org.example.warehouse2.services.StockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StockController.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private StockService stockService;
    @Mock
    private IngredientService ingredientService;
    @InjectMocks
    private StockController stockController;

    @Test
    public void testUpdateStock_Success() throws Exception {
        String ingredientNumber = "ING-001";
        int newStockCount = 50;
        Ingredient ingredient = new Ingredient(); // You might need to create a mock Ingredient
        ingredient.setIngredientNumber(ingredientNumber);

        when(ingredientService.getIngredientByNumber(ingredientNumber)).thenReturn(ingredient);
        when(stockService.updateStock(ingredientNumber, newStockCount)).thenReturn("Stock for ingredient ING-001 updated successfully");

        mockMvc.perform(post("/stock/update/" + ingredientNumber)
                        .param("newStockCount", String.valueOf(newStockCount))) // Note: convert int to String
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stock"))
                .andExpect(flash().attribute("message", "Stock for ingredient ING-001 updated successfully"));

        verify(stockService).updateStock(ingredientNumber, newStockCount);
    }

    @Test
    public void testUpdateStock_IngredientNotFound() throws Exception {
        String ingredientNumber = "ING-001";
        int newStockCount = 50;

        when(ingredientService.getIngredientByNumber(ingredientNumber))
                .thenThrow(new IngredientNotFoundException("Ingredient not found"));

        mockMvc.perform(post("/stock/update/" + ingredientNumber)
                        .param("newStockCount", String.valueOf(newStockCount)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stock"))
                .andExpect(flash().attribute("error", "Ingredient not found"));

        verify(stockService).updateStock(ingredientNumber, newStockCount);
    }

    @Test
    public void testUpdateStock_NegativeStockCount() throws Exception {
        String ingredientNumber = "ING-001";
        int newStockCount = -10;
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientNumber(ingredientNumber);

        when(ingredientService.getIngredientByNumber(ingredientNumber)).thenReturn(ingredient);

        mockMvc.perform(post("/stock/update/" + ingredientNumber)
                        .param("newStockCount", String.valueOf(newStockCount)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stock/edit/" + ingredientNumber))
                .andExpect(flash().attribute("error", "Stock count cannot be negative."));

        verify(stockService).updateStock(ingredientNumber, newStockCount);
    }


    @Test
    public void showUpdateStockFormTest() throws Exception {
        String ingredientNumber = "ING-001";
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientNumber(ingredientNumber);

        when(ingredientService.getIngredientByNumber(ingredientNumber)).thenReturn(ingredient);

        mockMvc.perform(get("/stock/edit/" + ingredientNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("stock/stock_update"))
                .andExpect(model().attributeExists("ingredient"));
    }
}
