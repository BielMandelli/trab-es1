package br.unioeste.padaria.receita.model.dto;

import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AtualizarReceitaDTO(
        String nomeReceita,
        @PositiveOrZero
        Integer rendimento,
        @PositiveOrZero
        Integer tempoPreparacao
) {
}
