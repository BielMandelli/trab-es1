package br.unioeste.padaria.receita.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public record AtualizarReceitaDTO(
        String nomeReceita,
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal rendimento,
        @PositiveOrZero
        Integer tempoPreparo,
        @PositiveOrZero
        Integer validade,
        Long idUnidadeMedida,
        List<AtualizarReceitaIngredienteDTO> ingredientes
) {
}
