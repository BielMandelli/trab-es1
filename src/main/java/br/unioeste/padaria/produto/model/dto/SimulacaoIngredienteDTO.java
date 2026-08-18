package br.unioeste.padaria.produto.model.dto;

import br.unioeste.padaria.ingrediente.model.entity.CategoriaIngrediente;
import br.unioeste.padaria.ingrediente.model.entity.UnidadeIngrediente;

import java.math.BigDecimal;

public record SimulacaoIngredienteDTO(
        Long idIngrediente,
        String nomeIngrediente,
        CategoriaIngrediente categoriaIngrediente,
        UnidadeIngrediente unidadeIngrediente,
        BigDecimal quantidadeNecessaria,
        BigDecimal estoqueAtual,
        BigDecimal saldoAposProducao,
        boolean suficiente
) {
}
