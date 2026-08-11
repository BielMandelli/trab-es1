package br.unioeste.padaria.recipe.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RecipeIngredientDTO(
        @NotNull
        Long ingredientId,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal quantity
) {
}
