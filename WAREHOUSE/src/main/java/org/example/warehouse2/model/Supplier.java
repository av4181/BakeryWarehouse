package org.example.warehouse2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_gen")
    @SequenceGenerator(name = "supplier_gen", sequenceName = "supplier_id_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column (name = "contactperson")
    private String contactPerson;
    @Column
    private String email;
    @Column (name = "phonenumber")
    private String phoneNumber;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> suppliedIngredients = new ArrayList<>();
}
