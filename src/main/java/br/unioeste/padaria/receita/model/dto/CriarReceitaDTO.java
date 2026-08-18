package br.unioeste.padaria.receita.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public record CriarReceitaDTO(
        @NotNull
        @NotEmpty
        String nomeReceita,
        @NotNull
        @PositiveOrZero
        Integer rendimento,
        @NotNull
        Long idUnidadeMedida,
        @NotNull
        @PositiveOrZero
        Integer tempoPreparo,
        @NotNull
        @PositiveOrZero
        Integer validade,
        List<@Valid CriarReceitaIngredienteDTO> ingredientes
) {
}
