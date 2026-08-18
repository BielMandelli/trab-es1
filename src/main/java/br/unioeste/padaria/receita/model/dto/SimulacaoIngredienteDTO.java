package br.unioeste.padaria.receita.model.dto;

import br.unioeste.padaria.ingrediente.model.entity.CategoriaIngrediente;
import br.unioeste.padaria.unidade.model.UnidadeMedida;

import java.math.BigDecimal;

public record SimulacaoIngredienteDTO(
        Long idIngrediente,
        String nomeIngrediente,
        CategoriaIngrediente categoriaIngrediente,
        UnidadeMedida unidadeMedida,
        BigDecimal quantidadeNecessaria,
        BigDecimal estoqueAtual,
        BigDecimal saldoAposProducao,
        boolean suficiente
) {
}
