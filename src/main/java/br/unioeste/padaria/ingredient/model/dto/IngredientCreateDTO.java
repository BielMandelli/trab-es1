package br.unioeste.padaria.ingredient.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record IngredientCreateDTO(
        @NotNull
        @NotEmpty
        String name,
        @NotNull
        Long idCategory,
        @NotNull
        Long idUnit,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal pricePerUnit,
        @NotNull
        @PositiveOrZero
        Integer stock,
        @NotNull
        @PositiveOrZero
        Integer minStock
) {
}
