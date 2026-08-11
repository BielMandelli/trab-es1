package br.unioeste.padaria.recipe.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RecipeCreateDTO(
        @NotNull @NotEmpty String name,
        @NotNull @PositiveOrZero BigDecimal sellingPrice,
        @NotNull @PositiveOrZero Integer perfomance,
        @NotNull @PositiveOrZero Integer preparationTime
) {
}
