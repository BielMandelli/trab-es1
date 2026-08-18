package br.unioeste.padaria.receita.model.dto;

import br.unioeste.padaria.ingrediente.model.entity.CategoriaIngrediente;
import br.unioeste.padaria.unidade.model.entity.UnidadeMedida;

import java.math.BigDecimal;

public record ReceitaIngredienteDTO(
        Long idIngrediente,
        String nomeIngrediente,
        CategoriaIngrediente categoriaIngrediente,
        UnidadeMedida unidadeMedida,
        BigDecimal custoPorUnidade,
        BigDecimal estoqueAtual,
        BigDecimal quantidade
) {
}
