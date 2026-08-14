package br.unioeste.padaria.receita.model.dto;

import java.math.BigDecimal;

public record SimulacaoIngredienteDTO(
        String nomeIngrediente,
        BigDecimal quantidadeNecessaria,
        BigDecimal estoqueAtual,
        BigDecimal saldoAposProducao,
        boolean suficiente
) {
}
