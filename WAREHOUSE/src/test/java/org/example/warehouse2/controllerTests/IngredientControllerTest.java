package org.example.warehouse2.controllerTests;

import org.example.warehouse2.controllers.api.dto.IngredientDTO;
import org.example.warehouse2.controllers.mvc.IngredientController;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.model.IngredientStatus;
import org.example.warehouse2.persistence.mappers.IngredientMapper;
import org.example.warehouse2.persistence.repositories.IngredientRepository;
import org.example.warehouse2.services.IngredientService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// US-58 Integration testing of Controllers
@AutoConfigureMockMvc
@SpringBootTest
//@WebMvcTest(IngredientController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientService ingredientService;

    @Mock
    private IngredientMapper ingredientMapper;
    @Mock
    private IngredientRepository ingredientRepository;

    @Test
    public void testGetAllIngredients() throws Exception {
        // Arrange
        List<IngredientDTO> mockIngredients = List.of(
                new IngredientDTO(1L, "ING-001", "Flour", 100, IngredientStatus.AVAILABLE,1L, LocalDate.now()),
                new IngredientDTO(2L, "ING-002", "Sugar", 50, IngredientStatus.OUT_OF_STOCK,2L, LocalDate.now().plusDays(10))
        );

        when(ingredientService.getIngredients()).thenReturn(mockIngredients);

        mockMvc.perform(get("/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("ingredient/ingredient_list"))
                .andExpect(model().attributeExists("ingredients"));
    }

    @Test
    void testShowIngredient_IngredientFound() throws Exception {
        Ingredient ingredient = new Ingredient(1L, "ING-001", "Flour", 100, IngredientStatus.AVAILABLE, null,LocalDate.now());

        when(ingredientService.getIngredientByNumber("ING-001")).thenReturn(ingredient);

        mockMvc.perform(get("/ingredients/{ingredientNumber}", "ING-001"))
                .andExpect(status().isOk())
                .andExpect(view().name("ingredient/ingredient_details"))
                .andExpect(model().attribute("ingredient", isA(IngredientDTO.class)));
    }

    @Test
    void testShowIngredient_IngredientNotFound() throws Exception {
        //TODO
    }

    @Test
    public void testGetExpiringIngredients() throws Exception {
        LocalDate expirationDate = LocalDate.now().plusDays(10);
        List<IngredientDTO> mockIngredients = List.of(
                new IngredientDTO(1L, "ING-001", "Flour", 100, IngredientStatus.AVAILABLE,1L, LocalDate.now().plusDays(2)),
                new IngredientDTO(2L, "ING-002", "Sugar", 50, IngredientStatus.OUT_OF_STOCK,2L, LocalDate.now().plusDays(3))
        );

        when(ingredientService.getIngredientsExpiringBefore(expirationDate)).thenReturn(mockIngredients);

        mockMvc.perform(get("/ingredients/expiring"))
                .andExpect(status().isOk())
                .andExpect(view().name("ingredient/ingredients_expiring"))
                .andExpect(model().attribute("expiringIngredients", mockIngredients));
    }
}

