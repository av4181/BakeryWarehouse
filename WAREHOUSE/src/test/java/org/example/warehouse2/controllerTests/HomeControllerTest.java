package org.example.warehouse2.controllerTests;

import org.example.warehouse2.controllers.api.dto.IngredientDTO;
import org.example.warehouse2.controllers.api.dto.OrderDTO;
import org.example.warehouse2.controllers.api.dto.SupplierDTO;
import org.example.warehouse2.controllers.mvc.HomeController;
import org.example.warehouse2.model.IngredientStatus;
import org.example.warehouse2.model.OrderStatus;
import org.example.warehouse2.services.IngredientService;
import org.example.warehouse2.services.OrderService;
import org.example.warehouse2.services.SupplierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientService ingredientService;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private OrderService orderService;

    @Test
    public void testHome() throws Exception {
        List<IngredientDTO> mockIngredients = List.of(
                new IngredientDTO(1L, "ING-001", "Flour", 100, IngredientStatus.AVAILABLE,1L, LocalDate.now().plusMonths(3)),
                new IngredientDTO(2L, "ING-002", "Sugar", 5, IngredientStatus.AVAILABLE, 2L, LocalDate.now().plusDays(2))
        );
        List<SupplierDTO> mockSuppliers = List.of(
                new SupplierDTO(1L, "Supplier A", "Alice", "alice@example.com", "1234567890"),
                new SupplierDTO(2L, "Supplier B", "Bob", "bob@example.com", "9876543210")
        );
        List<OrderDTO> mockOrders = List.of(
                new OrderDTO(UUID.randomUUID(), LocalDateTime.now(), OrderStatus.PENDING)
        );

        when(ingredientService.getIngredients()).thenReturn(mockIngredients);
        when(supplierService.getAllSuppliers()).thenReturn(mockSuppliers);
        when(orderService.getAllOrders()).thenReturn(mockOrders);

        mockMvc.perform(get("/")) // GET
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("totalIngredients", 2))
                .andExpect(model().attribute("lowStockCount", 1))
                .andExpect(model().attribute("expiringSoonCount", 1))
                .andExpect(model().attribute("recentOrders", hasSize(1)))
                .andExpect(model().attribute("pageTitle", "Warehouse Home"));
    }
}
