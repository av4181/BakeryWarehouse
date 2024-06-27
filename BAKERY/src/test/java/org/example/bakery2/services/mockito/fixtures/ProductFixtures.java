package org.example.bakery2.services.mockito.fixtures;

import org.example.bakery2.entities.Product;
import org.example.bakery2.entities.commands.product.CreateProductCommand;
import org.example.bakery2.entities.commands.product.UpdateProductCommand;

public class ProductFixtures {

    public static CreateProductCommand getCreateProductCommand() {
        return new CreateProductCommand("new Product");
    }

    public static Product getNewProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("New Product");
        product.setActive(false);

        return product;
    }

    public static Product getNewProductActive() {
        Product product = new Product();
        product.setId(1L);
        product.setName("New Product");
        product.setActive(true);

        return product;
    }

    public static CreateProductCommand getCreateProductCommandNoName() {
        return new CreateProductCommand("");
    }

    public static UpdateProductCommand getUpdateProductCommand() {
        return new UpdateProductCommand("New Product name");
    }

    public static UpdateProductCommand getUpdateProductCommandDuplicateName() {
        return new UpdateProductCommand("New Product");
    }

    public static UpdateProductCommand getUpdateProductCommandNoName() {
        return new UpdateProductCommand("");
    }
}
