package org.example.bakery2.entities.commands.recipe;

import org.example.bakery2.entities.Product;

public record CreateRecipeCommand(String name, Product product) {
}
