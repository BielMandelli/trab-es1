package br.unioeste.padaria.receita.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ReceitaIngredienteQuantidadeDTO(
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal quantidade
) {
}
