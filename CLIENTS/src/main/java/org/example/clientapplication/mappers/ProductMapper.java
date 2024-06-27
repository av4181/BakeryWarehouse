package org.example.clientapplication.mappers;

import org.example.clientapplication.dtos.ProductDto;
import org.example.clientapplication.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);
}
