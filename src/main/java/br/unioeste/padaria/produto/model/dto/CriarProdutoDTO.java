package br.unioeste.padaria.produto.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public record CriarProdutoDTO(
        @NotNull
        @NotEmpty
        String nomeProduto,
        @NotNull
        @PositiveOrZero
        BigDecimal precoVenda,
        @NotNull
        @PositiveOrZero
        Integer rendimento,
        @NotNull
        @PositiveOrZero
        Integer tempoPreparacao,
        List<@Valid CriarProdutoIngredienteDTO> ingredientes
) {
}
