package org.example.warehouse2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "ingredientnumber")
    private String ingredientNumber;
    @Column
    private String name;
    @Column
    private int stock;
    @Column
    @Enumerated(EnumType.STRING)
    private IngredientStatus status;

    @ManyToOne
    @JoinColumn(name = "supplierid", referencedColumnName = "id")
    private Supplier supplier;

    @Column (name = "expirationdate")
    private LocalDate expirationDate;

}
