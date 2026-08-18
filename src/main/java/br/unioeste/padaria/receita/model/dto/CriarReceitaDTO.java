package br.unioeste.padaria.receita.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record CriarReceitaDTO(
        @NotNull
        @NotEmpty
        String nomeReceita,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal rendimento,
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
