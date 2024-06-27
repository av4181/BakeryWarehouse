package org.example.bakery2.mappers;

import org.example.bakery2.entities.Ingredient;
import org.example.bakery2.entities.Product;
import org.example.bakery2.entities.commands.product.CreateProductCommand;
import org.example.bakery2.entities.commands.product.UpdateProductCommand;
import org.example.bakery2.entities.dto.ProductDTO;
import org.example.bakery2.entities.dto.ProductStateDTO;
import org.example.bakery2.services.IngredientService;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IngredientService.class})
public interface ProductMapper {

    Product convertToEntity(CreateProductCommand createProductCommand);

    Product convertToEntity(UpdateProductCommand updateProductCommand);

//    @Mappings({
//            @Mapping(target = "id", ignore = true),
//            @Mapping(target = "productName", ignore = true),
//            @Mapping(target = "price", ignore = true),
//            @Mapping(target = "ingredientList", ignore = true),
//            @Mapping(target = "productNumber", source = "uuid")
//    })
//    ProductDTO convertToDTO(Product product);

    default ProductDTO convertToDTO(Product product, List<Ingredient> ingredientList) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName(product.getName());
        productDTO.setProductNumber(product.getUuid().toString());
        productDTO.setIngredientList(ingredientList);
        return productDTO;
    }

    default ProductStateDTO convertToStateDTO(Product product) {
        ProductStateDTO productStateDTO = new ProductStateDTO();
        productStateDTO.setProductNumber(product.getUuid().toString());
        productStateDTO.setActive(product.isActive());
        return productStateDTO;
    }

    default Product modifyEntity(Product productToBeUpdated, UpdateProductCommand updateProductCommand) {
        productToBeUpdated.setName(updateProductCommand.name());

        return productToBeUpdated;
    }
}
