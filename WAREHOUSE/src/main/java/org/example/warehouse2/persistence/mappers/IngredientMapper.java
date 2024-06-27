package org.example.warehouse2.persistence.mappers;

import org.example.warehouse2.controllers.api.dto.IngredientDTO;
import org.example.warehouse2.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { SupplierMapper.class })
public interface IngredientMapper {
    // LET OP met mapping foreign keys.  Ingredient heeft een Supplier, we nemen supplier.id en
    // mappen naar supplierID van DTO maar ook de kolom naam in de DB
    @Mapping(source = "supplier.id", target = "supplierId")
    IngredientDTO toDTO(Ingredient ingredient);
    @Mapping(target = "id", ignore = true)
    void updateIngredientFromDTO(IngredientDTO ingredientDTO, @MappingTarget Ingredient ingredient);
    Ingredient toEntity(IngredientDTO ingredientDTO);

}

