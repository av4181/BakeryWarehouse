package org.example.warehouse2.controllers.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewSupplierDTO {
    @NotBlank(message = "Supplier name is mandatory")
    private String name;

    @NotBlank(message = "Contact person name is mandatory")
    private String contactPerson;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;
}
