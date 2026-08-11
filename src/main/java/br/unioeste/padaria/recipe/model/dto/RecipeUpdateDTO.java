package br.unioeste.padaria.recipe.model.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RecipeUpdateDTO(
        String name,
        @PositiveOrZero BigDecimal sellingPrice,
        @PositiveOrZero Integer perfomance,
        @PositiveOrZero Integer preparationTime
) {
}
