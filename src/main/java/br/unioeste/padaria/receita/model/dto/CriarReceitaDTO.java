package br.unioeste.padaria.receita.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CriarReceitaDTO(
        @NotNull
        @NotEmpty
        String nomeReceita,
        @NotNull
        @PositiveOrZero
        BigDecimal precoVenda,
        @NotNull
        @PositiveOrZero
        Integer rendimento,
        @NotNull
        @PositiveOrZero
        Integer tempoPreparacao
) {
}
