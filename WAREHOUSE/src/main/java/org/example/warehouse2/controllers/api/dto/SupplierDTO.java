package org.example.warehouse2.controllers.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    @Getter
    private Long id;
    private String name;
    private String contactPerson;
    private String email;
    private String phoneNumber;
}
