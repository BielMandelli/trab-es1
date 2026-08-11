package br.unioeste.padaria.ingredient.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record IngredientCategoryCreateDTO(
        @NotNull
        @NotEmpty
        String name
) {

}
