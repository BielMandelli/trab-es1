package br.unioeste.padaria.receita.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AtualizarReceitaDTO(
        String nomeReceita,
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal rendimento,
        @PositiveOrZero
        Integer tempoPreparo,
        @PositiveOrZero
        Integer validade,
        Long idUnidadeMedida
) {
}
