package org.example.clientapplication.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class ProductDto {

    private Long id;
    private String productNumber;
    private String productName;
    private Double price;
    private Boolean active;

}
