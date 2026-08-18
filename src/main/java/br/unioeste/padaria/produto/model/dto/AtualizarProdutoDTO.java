package br.unioeste.padaria.produto.model.dto;

import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AtualizarProdutoDTO(
        String nomeProduto,
        @PositiveOrZero
        BigDecimal custoVenda,
        @PositiveOrZero
        Integer rendimento,
        @PositiveOrZero
        Integer tempoPreparacao
) {
}
