package br.unioeste.padaria.ingredient.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record IngredientUnitCreateDTO (
        @NotNull
        @NotEmpty
        String name,
        @NotNull
        @NotEmpty
        String abbreviation
) {

}
