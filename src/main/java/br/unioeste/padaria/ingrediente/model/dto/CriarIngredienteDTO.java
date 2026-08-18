package br.unioeste.padaria.ingrediente.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CriarIngredienteDTO(
        @NotNull
        @NotEmpty
        String nomeIngrediente,
        @NotNull
        Long idCategoriaIngrediente,
        @NotNull
        Long idUnidadeMedida,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal custoPorUnidade,
        @NotNull
        @PositiveOrZero
        Integer estoqueAtual,
        @NotNull
        @PositiveOrZero
        Integer estoqueMinimo
) {
}
