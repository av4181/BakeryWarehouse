package org.example.warehouse2.persistence.mappers;
import org.example.warehouse2.controllers.api.dto.NewSupplierDTO;
import org.example.warehouse2.controllers.api.dto.SupplierDTO;
import org.example.warehouse2.model.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    //    @Mapping(target = "suppliedIngredients", ignore = true)
    SupplierDTO toDTO(Supplier supplier);
    Supplier toEntity(NewSupplierDTO newSupplierDTO);
    void updateSupplierFromDTO(SupplierDTO supplierDTO, @MappingTarget Supplier supplier);
    NewSupplierDTO toNewSupplierDTO(Supplier supplier);

}
