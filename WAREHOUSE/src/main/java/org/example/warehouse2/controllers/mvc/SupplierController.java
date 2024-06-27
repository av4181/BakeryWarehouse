package org.example.warehouse2.controllers.mvc;
import jakarta.validation.Valid;
import org.example.warehouse2.controllers.api.dto.NewSupplierDTO;
import org.example.warehouse2.controllers.api.dto.SupplierDTO;
import org.example.warehouse2.exceptions.SupplierNotFoundException;
import org.example.warehouse2.model.Supplier;
import org.example.warehouse2.persistence.mappers.SupplierMapper;
import org.example.warehouse2.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;
    private final SupplierMapper supplierMapper;

    @Autowired
    public SupplierController(SupplierService supplierService, SupplierMapper supplierMapper) {
        this.supplierService = supplierService;
        this.supplierMapper = supplierMapper;
    }

    // CRUD - Read
    @GetMapping
    public String getAllSuppliers(Model model) {
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        model.addAttribute("suppliers", suppliers);
        return "supplier/supplier_list";
    }

    // CRUD - Read - supplier DTO transfer naar service
    @GetMapping("/{id}")
    public String getSupplier(@PathVariable Long id, Model model) {
        try {
            SupplierDTO supplierDTO = supplierService.getSupplierById(id);
            model.addAttribute("supplier", supplierDTO);
            return "supplier/supplier_details";
        } catch (SupplierNotFoundException e) {
            // TODO error view
            throw e;
        }
    }

    @GetMapping("/add")
    public String showAddSupplierForm(Model model) {
        model.addAttribute("newSupplierDTO", new NewSupplierDTO());
        return "supplier/add_supplier";
    }

    // US-55 Leveranciers beheren en koppelen aan ingrediënten - Create - supplier DTO transfer
    @PostMapping("/add")
    public String processAddSupplier(@Valid @ModelAttribute("newSupplierDTO") NewSupplierDTO newSupplierDTO,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "supplier/add_supplier";
        } else {
            SupplierDTO supplierDTO = supplierService.createSupplier(newSupplierDTO);
            // TODO something
            return "redirect:/supplier";
        }
    }

    // US-55 Leveranciers beheren en koppelen aan ingrediënten - Update
    @GetMapping("/edit/{id}")
    public String showEditSupplierForm(@PathVariable Long id, Model model) {
        SupplierDTO supplierDTO = supplierService.getSupplierById(id);
        model.addAttribute("supplierDTO", supplierDTO);
        return "supplier/edit_supplier"; // Assuming you have an edit_supplier.html template
    }

    // US-55 Leveranciers beheren en koppelen aan ingrediënten - Update
    @PutMapping("/{id}")
    public String updateSupplier(@PathVariable Long id,
                                 @Valid @ModelAttribute("supplierDTO") SupplierDTO supplierDTO,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("supplierDTO", supplierDTO);
            return "supplier/add_supplier";
        }
        try {
            SupplierDTO updatedSupplier = supplierService.updateSupplier(id, supplierDTO);
            model.addAttribute("supplierDTO", updatedSupplier);
            model.addAttribute("message", "Supplier updated successfully");
            return "supplier/add_supplier";
        } catch (SupplierNotFoundException e) {
            model.addAttribute("error", "Supplier not found");
            return "error_page";
        }
    }

    // US-55 Leveranciers beheren en koppelen aan ingrediënten - Delete
    @DeleteMapping("/{id}")
    public String deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return "redirect:/supplier";
        } catch (SupplierNotFoundException e) {
            // TODO exception
            return "redirect:/supplier";
        }
    }
}
